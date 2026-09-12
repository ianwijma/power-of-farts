package com.ianwijma.poweroffarts.neoforge;

import net.minecraft.server.level.ServerPlayer;

import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import com.ianwijma.poweroffarts.command.PofCommands;
import com.ianwijma.poweroffarts.machine.PofBlockEntities;
import com.ianwijma.poweroffarts.player.DigestionSystem;

public class PofNeoForgeEvents {

    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity().level().isClientSide()) {
            return;
        }
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            DigestionSystem.tickPlayer(serverPlayer);
        }
    }

    public static void onRegisterCommands(RegisterCommandsEvent event) {
        PofCommands.register(event.getDispatcher());
    }

    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.Energy.BLOCK, PofBlockEntities.GENERATOR.get(),
                (generator, direction) -> new GeneratorEnergyHandler(generator));
        event.registerBlockEntity(Capabilities.Fluid.BLOCK, PofBlockEntities.PIPE.get(),
                (pipe, direction) -> new GasTankFluidHandler(pipe.getTank()));
        event.registerBlockEntity(Capabilities.Fluid.BLOCK, PofBlockEntities.GAS_TANK.get(),
                (tank, direction) -> new GasTankFluidHandler(tank.getTank()));
    }
}
