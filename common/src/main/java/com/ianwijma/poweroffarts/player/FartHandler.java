package com.ianwijma.poweroffarts.player;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;

import com.ianwijma.poweroffarts.Constants;
import com.ianwijma.poweroffarts.config.PofConfig;

public final class FartHandler {

    private FartHandler() {
    }

    public static void fart(ServerPlayer player, double ventedGas) {
        PofConfig config = PofConfig.get();
        AreaEffectCloud cloud = new AreaEffectCloud(player.level(), player.getX(), player.getY(), player.getZ());
        cloud.setRadius((float) config.fartCloudRadius);
        cloud.setDuration(config.fartCloudDurationSeconds * 20);
        cloud.setWaitTime(0);
        cloud.setOwner(player);
        cloud.addEffect(new MobEffectInstance(MobEffects.NAUSEA, config.nauseaSeconds * 20, 0));
        player.level().addFreshEntity(cloud);

        player.level().playSound(null, player.blockPosition(), SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 1.0F, 0.55F);

        Constants.LOG.info("{} farted out {} FL!", player.getGameProfile().name(), String.format("%.2f", ventedGas));
    }
}
