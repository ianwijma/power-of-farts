package com.ianwijma.poweroffarts.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.ianwijma.poweroffarts.config.PofConfig;
import com.ianwijma.poweroffarts.player.PlayerGasAccess;

@Mixin(Item.class)
public class ItemFinishUsingMixin {

    @Inject(method = "finishUsingItem", at = @At("TAIL"))
    private void pof$generateGas(ItemStack stack, Level level, LivingEntity entity, CallbackInfoReturnable<ItemStack> cir) {
        if (!(entity instanceof ServerPlayer player)) {
            return;
        }
        Consumable consumable = stack.get(DataComponents.CONSUMABLE);
        if (consumable == null) {
            return;
        }
        FoodProperties food = stack.get(DataComponents.FOOD);
        if (food == null) {
            return;
        }
        PofConfig config = PofConfig.get();
        double gas = (food.nutrition() + food.saturation()) * config.gasPerHunger;
        PlayerGasAccess.of(player).addPendingGas(gas);
    }
}
