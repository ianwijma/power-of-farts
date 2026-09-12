package com.ianwijma.poweroffarts.gas;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.world.level.material.Fluid;

public final class FluidGases {

    private static final Map<Fluid, Gas> INTERNED = new ConcurrentHashMap<>();

    public static Gas forFluid(Fluid fluid) {
        return INTERNED.computeIfAbsent(fluid, FluidGas::new);
    }

    private FluidGases() {
    }
}