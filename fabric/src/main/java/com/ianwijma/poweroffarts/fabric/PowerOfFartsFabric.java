package com.ianwijma.poweroffarts.fabric;

import com.ianwijma.poweroffarts.Constants;
import com.ianwijma.poweroffarts.PofRegistries;
import com.ianwijma.poweroffarts.PowerOfFarts;
import com.ianwijma.poweroffarts.config.PofConfig;
import com.ianwijma.poweroffarts.player.DigestionSystem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.level.ServerPlayer;

public class PowerOfFartsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        PofRegistries.init();
        PofConfig.load();
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                DigestionSystem.tickPlayer(player);
            }
        });
        Constants.LOG.info("Hello Fabric world!");
        PowerOfFarts.init();
    }
}
