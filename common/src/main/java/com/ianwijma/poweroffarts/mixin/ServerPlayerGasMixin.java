package com.ianwijma.poweroffarts.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.ianwijma.poweroffarts.player.PlayerGas;
import com.ianwijma.poweroffarts.player.PlayerGasAccess;

@Mixin(ServerPlayer.class)
public class ServerPlayerGasMixin implements PlayerGasAccess {

    @Unique
    private final PlayerGas pof$gasData = new PlayerGas();

    @Override
    public PlayerGas pof$getGasData() {
        return pof$gasData;
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void pof$saveGas(ValueOutput output, CallbackInfo ci) {
        pof$gasData.save(output.child("poweroffarts"));
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void pof$readGas(ValueInput input, CallbackInfo ci) {
        pof$gasData.read(input.childOrEmpty("poweroffarts"));
    }
}
