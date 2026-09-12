package com.ianwijma.poweroffarts.neoforge;

import net.minecraft.server.level.ServerPlayer;

import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import com.ianwijma.poweroffarts.command.PofCommands;
import com.ianwijma.poweroffarts.player.DigestionSystem;

public class PofNeoForgeEvents {

    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity().level().isClientSide()) {
            return;
        }
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            DigestionSystem.tickPlayer(serverPlayer);
        }
    }

    public static void onRegisterCommands(RegisterCommandsEvent event) {
        PofCommands.register(event.getDispatcher());
    }
}
