package com.ianwijma.poweroffarts.fabric;

import java.util.Iterator;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;

import net.minecraft.world.level.material.Fluid;

import com.ianwijma.poweroffarts.gas.FluidGas;
import com.ianwijma.poweroffarts.gas.FluidGases;
import com.ianwijma.poweroffarts.gas.GasTank;
import com.ianwijma.poweroffarts.gas.PofGases;

/**
 * Single-slot fluid view over a GasTank. Accepts only fluids tagged
 * {@code #poweroffarts:gas_fluids}, converting 1 mB = 1 FL.
 *
 * NOTE (early concept): mutations are applied directly instead of snapshotting,
 * so transactional aborts by foreign mods are not rolled back.
 */
public class GasTankFluidStorage implements Storage<FluidVariant> {

    private final GasTank tank;

    public GasTankFluidStorage(GasTank tank) {
        this.tank = tank;
    }

    @Override
    public long insert(FluidVariant variant, long maxAmount, net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext transaction) {
        Fluid fluid = variant.getFluid();
        if (!PofGases.isGasFluid(fluid) || maxAmount <= 0) {
            return 0;
        }
        return (long) tank.insert(FluidGases.forFluid(fluid), maxAmount, false);
    }

    @Override
    public long extract(FluidVariant variant, long maxAmount, net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext transaction) {
        if (!(tank.getGas() instanceof FluidGas fluidGas) || maxAmount <= 0) {
            return 0;
        }
        if (fluidGas.getFluid() != variant.getFluid()) {
            return 0;
        }
        return (long) tank.extract(maxAmount, false);
    }

    @Override
    public Iterator<StorageView<FluidVariant>> iterator() {
        return java.util.List.<StorageView<FluidVariant>>of(new StorageView<FluidVariant>() {
            @Override
            public long extract(FluidVariant variant, long maxAmount, net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext transaction) {
                return GasTankFluidStorage.this.extract(variant, maxAmount, transaction);
            }

            @Override
            public boolean isResourceBlank() {
                return tank.isEmpty();
            }

            @Override
            public FluidVariant getResource() {
                return tank.getGas() instanceof FluidGas fluidGas ? FluidVariant.of(fluidGas.getFluid()) : FluidVariant.blank();
            }

            @Override
            public long getAmount() {
                return (long) tank.getAmount();
            }

            @Override
            public long getCapacity() {
                return (long) tank.getCapacity();
            }
        }).iterator();
    }
}