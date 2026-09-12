package com.ianwijma.poweroffarts.platform.services;

import java.nio.file.Path;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import com.ianwijma.poweroffarts.gas.Gas;

import net.minecraft.world.level.material.Fluid;

public interface IPlatformHelper {

    String getPlatformName();

    boolean isModLoaded(String modId);

    boolean isDevelopmentEnvironment();

    Path getConfigDir();

    ServerPlayer createFakePlayer(ServerLevel level);

    long pushEnergy(ServerLevel level, BlockPos sourcePos, Direction direction, long amount);

    double pushGasToFluidTank(ServerLevel level, BlockPos targetPos, Direction face, Gas gas, double amount);

    double insertGasFluid(ServerLevel level, BlockPos targetPos, Direction face, Fluid fluid, int amount);

    default String getEnvironmentName() {
        return isDevelopmentEnvironment() ? "development" : "production";
    }
}
