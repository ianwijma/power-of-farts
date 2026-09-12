package com.ianwijma.poweroffarts.gas;

import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

import com.ianwijma.poweroffarts.Constants;

public final class PofGases {

    public static final Supplier<Gas> FARTS = register(Gases.REGISTRY, "farts");

    public static void touch() {
    }

    private static Supplier<Gas> register(Registry<Gas> registry, String name) {
        Gas gas = Registry.register(registry, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name), new Gas());
        return () -> gas;
    }
}
