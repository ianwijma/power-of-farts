package com.ianwijma.poweroffarts.neoforge;

import net.minecraft.server.level.ServerPlayer;

import net.neoforged.neoforge.network.PacketDistributor;

import com.ianwijma.poweroffarts.network.PlayerGasPayload;
import com.ianwijma.poweroffarts.platform.IPlatformNetwork;

public class NeoForgeNetworkService implements IPlatformNetwork {

    @Override
    public void sendToPlayer(ServerPlayer player, PlayerGasPayload payload) {
        PacketDistributor.sendToPlayer(player, payload);
    }
}
