package com.ianwijma.poweroffarts.player;

import net.minecraft.server.level.ServerPlayer;

import com.ianwijma.poweroffarts.config.PofConfig;
import com.ianwijma.poweroffarts.network.PlayerGasPayload;
import com.ianwijma.poweroffarts.platform.Services;

public final class DigestionSystem {

    private DigestionSystem() {
    }

    public static void tickPlayer(ServerPlayer player) {
        PlayerGas gas = PlayerGasAccess.of(player);
        PofConfig config = PofConfig.get();
        double perTick = config.digestionRatePerSecond / 20.0;
        gas.tickDigestion(perTick, config.playerGasCapacity, vented -> FartHandler.fart(player, vented));
        gas.syncIfNeeded(player);
    }
}
