package com.ianwijma.poweroffarts.fabric;

import com.ianwijma.poweroffarts.Constants;
import com.ianwijma.poweroffarts.PofRegistries;
import com.ianwijma.poweroffarts.PowerOfFarts;
import com.ianwijma.poweroffarts.command.PofCommands;
import com.ianwijma.poweroffarts.config.PofConfig;
import com.ianwijma.poweroffarts.player.DigestionSystem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.level.ServerPlayer;

public class PowerOfFartsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        PofRegistries.init();
        PofCreativeTabs.touch();
        PofConfig.load();
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                DigestionSystem.tickPlayer(player);
            }
        });
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                PofCommands.register(dispatcher));
        net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry.clientboundPlay().register(
                com.ianwijma.poweroffarts.network.PlayerGasPayload.TYPE,
                com.ianwijma.poweroffarts.network.PlayerGasPayload.STREAM_CODEC);
        Constants.LOG.info("Hello Fabric world!");
        PowerOfFarts.init();
    }
}
