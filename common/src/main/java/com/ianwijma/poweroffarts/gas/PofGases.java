package com.ianwijma.poweroffarts.gas;

import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

import com.ianwijma.poweroffarts.Constants;

public final class PofGases {

    public static final TagKey<Fluid> GAS_FLUIDS_TAG =
            TagKey.create(Registries.FLUID, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "gas_fluids"));

    public static boolean isGasFluid(Fluid fluid) {
        return fluid.is(GAS_FLUIDS_TAG);
    }

    public static final Supplier<Gas> FARTS = register(Gases.REGISTRY, "farts");

    public static void touch() {
    }

    private static Supplier<Gas> register(Registry<Gas> registry, String name) {
        Gas gas = Registry.register(registry, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name), new Gas());
        return () -> gas;
    }
}
