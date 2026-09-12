package com.ianwijma.poweroffarts.neoforge;

import com.ianwijma.poweroffarts.platform.services.IPlatformHelper;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import java.nio.file.Path;
import java.util.UUID;

import com.ianwijma.poweroffarts.gas.Gas;
import com.ianwijma.poweroffarts.platform.services.IPlatformHelper;

public class NeoForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLEnvironment.isProduction();
    }

    @Override
    public Path getConfigDir() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public ServerPlayer createFakePlayer(ServerLevel level) {
        return FakePlayerFactory.get(level, new GameProfile(UUID.randomUUID(), "pof-validator"));
    }

    @Override
    public long pushEnergy(ServerLevel level, BlockPos sourcePos, Direction direction, long amount) {
        EnergyHandler target = level.getCapability(Capabilities.Energy.BLOCK, sourcePos.relative(direction), direction.getOpposite());
        if (target == null || amount <= 0) {
            return 0;
        }
        try (Transaction transaction = Transaction.openRoot()) {
            int inserted = target.insert((int) Math.min(amount, Integer.MAX_VALUE), transaction);
            if (inserted > 0) {
                transaction.commit();
            }
            return inserted;
        }
    }

    @Override
    public double pushGasToFluidTank(ServerLevel level, BlockPos targetPos, Direction face, Gas gas, double amount) {
        if (!(gas instanceof com.ianwijma.poweroffarts.gas.FluidGas fluidGas) || amount <= 0) {
            return 0;
        }
        ResourceHandler<FluidResource> target = level.getCapability(Capabilities.Fluid.BLOCK, targetPos, face);
        if (target == null) {
            return 0;
        }
        try (Transaction transaction = Transaction.openRoot()) {
            int inserted = ResourceHandlerUtil.insertStacking(target,
                    FluidResource.of(fluidGas.getFluid()), (int) Math.min(amount, Integer.MAX_VALUE), transaction);
            if (inserted > 0) {
                transaction.commit();
            }
            return inserted;
        }
    }

    @Override
    public double insertGasFluid(ServerLevel level, BlockPos targetPos, Direction face, Fluid fluid, int amount) {
        ResourceHandler<FluidResource> target = level.getCapability(Capabilities.Fluid.BLOCK, targetPos, face);
        if (target == null || amount <= 0) {
            return 0;
        }
        try (Transaction transaction = Transaction.openRoot()) {
            int inserted = ResourceHandlerUtil.insertStacking(target, FluidResource.of(fluid), amount, transaction);
            if (inserted > 0) {
                transaction.commit();
            }
            return inserted;
        }
    }
}
