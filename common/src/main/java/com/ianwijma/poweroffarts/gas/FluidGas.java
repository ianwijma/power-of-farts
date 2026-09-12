package com.ianwijma.poweroffarts.gas;

import net.minecraft.world.level.material.Fluid;

/**
 * A gas that originates from (and converts back to) a mod fluid.
 * 1 mB of fluid = 1 FL of gas.
 */
public class FluidGas extends Gas {

    private final Fluid fluid;

    FluidGas(Fluid fluid) {
        this.fluid = fluid;
    }

    public Fluid getFluid() {
        return fluid;
    }
}