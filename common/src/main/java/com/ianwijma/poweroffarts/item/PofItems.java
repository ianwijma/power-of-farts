package com.ianwijma.poweroffarts.item;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import com.ianwijma.poweroffarts.platform.Services;

import java.util.function.Supplier;

public final class PofItems {

    public static final Supplier<Item> GAS_BAG = Services.REGISTRY.register(BuiltInRegistries.ITEM, "gas_bag", id ->
            new GasBagItem(new Item.Properties().stacksTo(1).setId(ResourceKey.create(Registries.ITEM, id))));

    public static void touch() {
    }
}
