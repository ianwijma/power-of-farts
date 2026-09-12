package com.ianwijma.poweroffarts.neoforge;

import java.util.function.Supplier;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;

import com.ianwijma.poweroffarts.item.PofItems;
import com.ianwijma.poweroffarts.machine.PofBlocks;
import com.ianwijma.poweroffarts.platform.Services;

public final class PofCreativeTabs {

    public static final Supplier<CreativeModeTab> MAIN = Services.REGISTRY.register(BuiltInRegistries.CREATIVE_MODE_TAB, "main", id ->
            CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                    .title(Component.translatable("itemGroup.poweroffarts"))
                    .icon(() -> new ItemStack(PofItems.GAS_BAG.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(PofItems.GAS_BAG.get());
                        output.accept(new ItemStack(PofBlocks.DEPOSITOR.get()));
                        output.accept(new ItemStack(PofBlocks.PIPE.get()));
                        output.accept(new ItemStack(PofBlocks.GAS_TANK.get()));
                        output.accept(new ItemStack(PofBlocks.GENERATOR.get()));
                    })
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .build());

    public static void touch() {
    }
}
