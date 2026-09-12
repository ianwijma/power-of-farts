package com.ianwijma.poweroffarts.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import com.ianwijma.poweroffarts.config.PofConfig;
import com.ianwijma.poweroffarts.player.PlayerGas;
import com.ianwijma.poweroffarts.player.PlayerGasAccess;

import java.util.function.Consumer;

public class GasBagItem extends Item {

    private static final int BAR_COLOR = 0x9CCB3B;

    public GasBagItem(Properties properties) {
        super(properties);
    }

    public static double getGas(ItemStack stack) {
        return stack.getOrDefault(PofDataComponents.GAS_AMOUNT.get(), 0.0);
    }

    public static void setGas(ItemStack stack, double amount) {
        stack.set(PofDataComponents.GAS_AMOUNT.get(), Math.max(0, amount));
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            PlayerGas gas = PlayerGasAccess.of(player);
            PofConfig config = PofConfig.get();
            if (player.isShiftKeyDown()) {
                double released = getGas(stack);
                if (released > 0) {
                    double filled = gas.fillStored(released, config.playerGasCapacity);
                    setGas(stack, released - filled);
                    level.playSound(null, player.blockPosition(), SoundEvents.BOTTLE_EMPTY, SoundSource.PLAYERS, 0.8F, 1.2F);
                }
            } else {
                double stored = gas.getStoredGas();
                if (stored > 0) {
                    double bagSpace = config.gasBagCapacity - getGas(stack);
                    double collected = gas.drainStored(Math.min(stored, bagSpace));
                    setGas(stack, getGas(stack) + collected);
                    level.playSound(null, player.blockPosition(), SoundEvents.BOTTLE_FILL, SoundSource.PLAYERS, 0.8F, 0.8F);
                }
            }
            return InteractionResult.SUCCESS_SERVER;
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return getGas(stack) > 0;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        double capacity = PofConfig.get().gasBagCapacity;
        return Math.round((float) (getGas(stack) / capacity * 13.0));
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return BAR_COLOR;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        double amount = getGas(stack);
        double capacity = PofConfig.get().gasBagCapacity;
        tooltip.accept(Component.translatable("tooltip.poweroffarts.gas_bag.contents", String.format("%.0f", amount), String.format("%.0f", capacity))
                .withStyle(ChatFormatting.GREEN));
        tooltip.accept(Component.translatable("tooltip.poweroffarts.gas_bag.use").withStyle(ChatFormatting.GRAY));
    }
}
