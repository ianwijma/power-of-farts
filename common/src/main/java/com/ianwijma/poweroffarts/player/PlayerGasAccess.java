package com.ianwijma.poweroffarts.player;

import net.minecraft.world.entity.player.Player;

public interface PlayerGasAccess {

    PlayerGas pof$getGasData();

    static PlayerGas of(Player player) {
        return player instanceof PlayerGasAccess access ? access.pof$getGasData() : new PlayerGas();
    }
}
