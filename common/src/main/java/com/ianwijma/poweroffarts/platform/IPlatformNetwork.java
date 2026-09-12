package com.ianwijma.poweroffarts.platform;

import net.minecraft.server.level.ServerPlayer;

import com.ianwijma.poweroffarts.network.PlayerGasPayload;

public interface IPlatformNetwork {

    void sendToPlayer(ServerPlayer player, PlayerGasPayload payload);
}
