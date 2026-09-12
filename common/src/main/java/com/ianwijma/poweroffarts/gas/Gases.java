package com.ianwijma.poweroffarts.gas;

import net.minecraft.resources.ResourceKey;
import net.minecraft.core.Registry;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;

import com.mojang.serialization.Lifecycle;

import com.ianwijma.poweroffarts.Constants;

public final class Gases {

    public static final ResourceKey<Registry<Gas>> KEY =
            ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "gases"));

    public static final Registry<Gas> REGISTRY = new MappedRegistry<>(KEY, Lifecycle.stable(), false);

    private Gases() {
    }
}
