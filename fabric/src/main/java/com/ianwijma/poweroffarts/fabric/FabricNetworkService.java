package com.ianwijma.poweroffarts.fabric;

import net.minecraft.server.level.ServerPlayer;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import com.ianwijma.poweroffarts.network.PlayerGasPayload;
import com.ianwijma.poweroffarts.platform.IPlatformNetwork;

public class FabricNetworkService implements IPlatformNetwork {

    @Override
    public void sendToPlayer(ServerPlayer player, PlayerGasPayload payload) {
        net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(player, payload);
    }
}
