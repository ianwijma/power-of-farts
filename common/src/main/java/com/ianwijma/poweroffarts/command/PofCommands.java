package com.ianwijma.poweroffarts.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;

import com.ianwijma.poweroffarts.config.PofConfig;
import com.ianwijma.poweroffarts.gas.Gases;
import com.ianwijma.poweroffarts.platform.Services;
import com.ianwijma.poweroffarts.player.DigestionSystem;
import com.ianwijma.poweroffarts.player.PlayerGas;
import com.ianwijma.poweroffarts.player.PlayerGasAccess;

public final class PofCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("pof")
                .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_ADMIN));

        root.then(Commands.literal("registry").executes(context -> {
            StringBuilder report = new StringBuilder();
            report.append("\nItems: ");
            BuiltInRegistries.ITEM.keySet().stream()
                    .filter(id -> id.getNamespace().equals("poweroffarts"))
                    .forEach(id -> report.append(id).append("  "));
            report.append("\nGases: ");
            com.ianwijma.poweroffarts.gas.Gases.REGISTRY.keySet().forEach(id -> report.append(id).append("  "));
            report.append("\nDataComponents: ");
            BuiltInRegistries.DATA_COMPONENT_TYPE.keySet().stream()
                    .filter(id -> id.getNamespace().equals("poweroffarts"))
                    .forEach(id -> report.append(id).append("  "));
            context.getSource().sendSuccess(() -> Component.literal(report.toString()), false);
            return 1;
        }));

        root.then(Commands.literal("gas")
                .then(Commands.argument("amount", DoubleArgumentType.doubleArg(0))
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            double amount = DoubleArgumentType.getDouble(context, "amount");
                            PlayerGasAccess.of(player).fillStored(amount, PofConfig.get().playerGasCapacity);
                            context.getSource().sendSuccess(() -> Component.literal(
                                    String.format("Stored gas set-ish: now %.1f FL", PlayerGasAccess.of(player).getStoredGas())), false);
                            return 1;
                        })));

        root.then(Commands.literal("pending")
                .then(Commands.argument("amount", DoubleArgumentType.doubleArg(0))
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            double amount = DoubleArgumentType.getDouble(context, "amount");
                            PlayerGasAccess.of(player).addPendingGas(amount);
                            context.getSource().sendSuccess(() -> Component.literal(
                                    String.format("Pending gas: now %.1f FL", PlayerGasAccess.of(player).getPendingGas())), false);
                            return 1;
                        })));

        root.then(Commands.literal("flowtest").executes(context -> {
            CommandSourceStack source = context.getSource();
            var level = source.getLevel();
            BlockPos base = BlockPos.containing(source.getPosition()).above(2);

            level.setBlock(base, com.ianwijma.poweroffarts.machine.PofBlocks.DEPOSITOR.get().defaultBlockState(), 3);
            level.setBlock(base.east(), com.ianwijma.poweroffarts.machine.PofBlocks.PIPE.get().defaultBlockState(), 3);
            level.setBlock(base.east(2), com.ianwijma.poweroffarts.machine.PofBlocks.GAS_TANK.get().defaultBlockState(), 3);
            level.setBlock(base.east(3), com.ianwijma.poweroffarts.machine.PofBlocks.GENERATOR.get().defaultBlockState(), 3);

            var depositor = (com.ianwijma.poweroffarts.machine.GasDepositorBlockEntity) level.getBlockEntity(base);
            var pipe = (com.ianwijma.poweroffarts.machine.GasPipeBlockEntity) level.getBlockEntity(base.east());
            var tank = (com.ianwijma.poweroffarts.machine.GasTankBlockEntity) level.getBlockEntity(base.east(2));
            var generator = (com.ianwijma.poweroffarts.machine.FartGeneratorBlockEntity) level.getBlockEntity(base.east(3));
            if (depositor == null || pipe == null || tank == null || generator == null) {
                source.sendFailure(Component.literal("flowtest: block entities missing"));
                return 0;
            }

            // Simulate a full gas bag inserted into the depositor
            double inserted = 500.0;
            com.ianwijma.poweroffarts.gas.Gas farts = com.ianwijma.poweroffarts.gas.PofGases.FARTS.get();
            double remaining = inserted;
            for (int tick = 0; tick < 600; tick++) {
                if (remaining > 0) {
                    double drained = Math.min(remaining, com.ianwijma.poweroffarts.config.PofConfig.get().depositorTransferRate);
                    double accepted = depositor.getTank().insert(farts, drained, false);
                    remaining -= accepted;
                }
                depositor.serverTick(level);
                pipe.serverTick(level);
                tank.serverTick(level);
            }
            double depEnd = depositor.getTank().getAmount();
            double pipeEnd = pipe.getTank().getAmount();
            double tankEnd = tank.getTank().getAmount();
            double leftover = remaining;
            boolean transportOk = Math.abs(depEnd + pipeEnd + tankEnd + leftover - inserted) < 0.5;
            source.sendSuccess(() -> Component.literal(String.format(
                    "transport: dep %.1f, pipe %.1f, tank %.1f, undelivered %.1f | conservation %s",
                    depEnd, pipeEnd, tankEnd, leftover, transportOk ? "OK" : "FAIL")), false);

            // Phase 2: generator burns gas into energy
            generator.getTank().insert(farts, 100.0, false);
            for (int tick = 0; tick < 100; tick++) {
                generator.serverTick(level);
            }
            double genGasLeft = generator.getTank().getAmount();
            long genEnergy = generator.getEnergy().getEnergyStored();
            boolean genOk = Math.abs((100.0 - genGasLeft) * 5.0 - genEnergy) < 1.0;
            source.sendSuccess(() -> Component.literal(String.format(
                    "generator: gas %.1f/100, energy %d FE | burn %s",
                    genGasLeft, genEnergy, genOk ? "OK" : "FAIL")), false);
            return 1;
        }));

        root.then(Commands.literal("validate").executes(context -> {
            CommandSourceStack source = context.getSource();
            ServerPlayer fakePlayer = Services.PLATFORM.createFakePlayer(source.getLevel());
            PlayerGas gas = PlayerGasAccess.of(fakePlayer);
            PofConfig config = PofConfig.get();

            gas.addPendingGas(config.playerGasCapacity * 1.5);
            double pendingBefore = gas.getPendingGas();

            int ticks = 0;
            while (gas.getPendingGas() > 0 && ticks < 100_000) {
                DigestionSystem.tickPlayer(fakePlayer);
                ticks++;
            }
            int elapsedTicks = ticks;

            source.sendSuccess(() -> Component.literal(String.format(
                    "P.O.F. validate: pending %.1f FL -> stored %.1f FL (capacity %.0f), fart=%s (%d ticks), platform=%s",
                    pendingBefore, gas.getStoredGas(), config.playerGasCapacity,
                    gas.isCoolingDown() ? "YES" : "no", elapsedTicks, Services.PLATFORM.getPlatformName())), false);
            return 1;
        }));

        dispatcher.register(root);
    }

    private PofCommands() {
    }
}
