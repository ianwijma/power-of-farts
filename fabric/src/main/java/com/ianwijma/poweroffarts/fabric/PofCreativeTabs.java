package com.ianwijma.poweroffarts.fabric;

import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import com.ianwijma.poweroffarts.Constants;
import com.ianwijma.poweroffarts.item.PofItems;

public final class PofCreativeTabs {

    public static final Supplier<CreativeModeTab> MAIN = register("main",
            new CreativeModeTab.Builder(CreativeModeTab.Row.TOP, 0)
                    .title(Component.translatable("itemGroup.poweroffarts"))
                    .icon(() -> new ItemStack(PofItems.GAS_BAG.get()))
                    .displayItems((parameters, output) -> output.accept(PofItems.GAS_BAG.get()))
                    .build());

    public static void touch() {
    }

    private static Supplier<CreativeModeTab> register(String name, CreativeModeTab tab) {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name), tab);
        return () -> tab;
    }
}
