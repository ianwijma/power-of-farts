package com.ianwijma.poweroffarts.neoforge;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import com.ianwijma.poweroffarts.gas.FluidGases;
import com.ianwijma.poweroffarts.gas.GasTank;
import com.ianwijma.poweroffarts.gas.PofGases;
import net.minecraft.world.level.material.Fluid;

/**
 * Single-slot fluid view over a machine's GasTank. Accepts only fluids tagged
 * {@code #poweroffarts:gas_fluids}, converting 1 mB = 1 FL. Extraction is
 * allowed so foreign pumps can drain FluidGas back out.
 *
 * NOTE (early concept): mutations are applied directly instead of snapshotting,
 * so transactional aborts by foreign mods are not rolled back.
 */
public class GasTankFluidHandler implements ResourceHandler<FluidResource> {

    private final GasTank tank;

    public GasTankFluidHandler(GasTank tank) {
        this.tank = tank;
    }

    private static Fluid fluidOf(FluidResource resource) {
        return resource.getFluid();
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public FluidResource getResource(int index) {
        if (tank.getGas() instanceof com.ianwijma.poweroffarts.gas.FluidGas fluidGas) {
            return FluidResource.of(fluidGas.getFluid());
        }
        return FluidResource.EMPTY;
    }

    @Override
    public long getAmountAsLong(int index) {
        return (long) tank.getAmount();
    }

    @Override
    public long getCapacityAsLong(int index, FluidResource resource) {
        return (long) tank.getCapacity();
    }

    @Override
    public boolean isValid(int index, FluidResource resource) {
        return resource != null && !resource.isEmpty() && PofGases.isGasFluid(resource.getFluid());
    }

    @Override
    public int insert(int index, FluidResource resource, int amount, TransactionContext context) {
        if (!isValid(index, resource) || amount <= 0) {
            return 0;
        }
        return (int) tank.insert(FluidGases.forFluid(resource.getFluid()), amount, false);
    }

    @Override
    public int extract(int index, FluidResource resource, int amount, TransactionContext context) {
        if (amount <= 0 || !(tank.getGas() instanceof com.ianwijma.poweroffarts.gas.FluidGas fluidGas)) {
            return 0;
        }
        if (fluidGas.getFluid() != fluid(resource)) {
            return 0;
        }
        return (int) tank.extract(amount, false);
    }

    private static Fluid fluid(FluidResource resource) {
        return resource.getFluid();
    }
}