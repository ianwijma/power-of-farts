package com.ianwijma.poweroffarts.fabric;

import com.ianwijma.poweroffarts.platform.services.IPlatformHelper;
import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.material.Fluid;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import team.reborn.energy.api.EnergyStorage;

import java.nio.file.Path;
import java.util.UUID;

import com.ianwijma.poweroffarts.gas.Gas;
import com.ianwijma.poweroffarts.gas.FluidGas;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public Path getConfigDir() {
        return FabricLoader.getInstance().getConfigDir();
    }

    @Override
    public ServerPlayer createFakePlayer(ServerLevel level) {
        return FakePlayer.get(level, new GameProfile(UUID.randomUUID(), "pof-validator"));
    }

    @Override
    public long pushEnergy(ServerLevel level, BlockPos sourcePos, Direction direction, long amount) {
        EnergyStorage target = EnergyStorage.SIDED.find(level, sourcePos.relative(direction), direction.getOpposite());
        if (target == null || amount <= 0) {
            return 0;
        }
        try (net.fabricmc.fabric.api.transfer.v1.transaction.Transaction transaction =
                     net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()) {
            long inserted = target.insert(amount, transaction);
            if (inserted > 0) {
                transaction.commit();
            }
            return inserted;
        }
    }

    @Override
    public double pushGasToFluidTank(ServerLevel level, BlockPos targetPos, Direction face, Gas gas, double amount) {
        if (!(gas instanceof FluidGas fluidGas) || amount <= 0) {
            return 0;
        }
        Storage<FluidVariant> target = FluidStorage.SIDED.find(level, targetPos, face);
        if (target == null) {
            return 0;
        }
        try (net.fabricmc.fabric.api.transfer.v1.transaction.Transaction transaction =
                     net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()) {
            long inserted = target.insert(FluidVariant.of(fluidGas.getFluid()), (long) amount, transaction);
            if (inserted > 0) {
                transaction.commit();
            }
            return inserted;
        }
    }

    @Override
    public double insertGasFluid(ServerLevel level, BlockPos targetPos, Direction face, Fluid fluid, int amount) {
        Storage<FluidVariant> target = FluidStorage.SIDED.find(level, targetPos, face);
        if (target == null || amount <= 0) {
            return 0;
        }
        try (net.fabricmc.fabric.api.transfer.v1.transaction.Transaction transaction =
                     net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()) {
            long inserted = target.insert(FluidVariant.of(fluid), amount, transaction);
            if (inserted > 0) {
                transaction.commit();
            }
            return inserted;
        }
    }
}
