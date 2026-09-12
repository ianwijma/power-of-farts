package com.ianwijma.poweroffarts.platform.services;

import java.nio.file.Path;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public interface IPlatformHelper {

    String getPlatformName();

    boolean isModLoaded(String modId);

    boolean isDevelopmentEnvironment();

    Path getConfigDir();

    ServerPlayer createFakePlayer(ServerLevel level);

    long pushEnergy(ServerLevel level, BlockPos sourcePos, Direction direction, long amount);

    default String getEnvironmentName() {
        return isDevelopmentEnvironment() ? "development" : "production";
    }
}
