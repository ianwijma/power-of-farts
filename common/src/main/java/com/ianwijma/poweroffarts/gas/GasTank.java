package com.ianwijma.poweroffarts.gas;

import org.jspecify.annotations.Nullable;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import com.ianwijma.poweroffarts.Constants;

public class GasTank {

    private static final String TAG_GAS = "gas";
    private static final String TAG_AMOUNT = "amount";

    @Nullable
    private Gas gas;
    private double amount;
    private final double capacity;

    public GasTank(double capacity) {
        this.capacity = capacity;
    }

    public double getCapacity() {
        return capacity;
    }

    public double getAmount() {
        return amount;
    }

    @Nullable
    public Gas getGas() {
        return gas;
    }

    public boolean isEmpty() {
        return gas == null || amount <= 0;
    }

    public double insert(Gas gas, double maxAmount, boolean simulate) {
        if (maxAmount <= 0) {
            return 0;
        }
        if (this.gas != null && this.gas != gas) {
            return 0;
        }
        double accepted = Math.min(maxAmount, capacity - amount);
        if (accepted <= 0) {
            return 0;
        }
        if (!simulate) {
            this.gas = gas;
            amount += accepted;
        }
        return accepted;
    }

    public double extract(double maxAmount, boolean simulate) {
        if (maxAmount <= 0 || isEmpty()) {
            return 0;
        }
        double extracted = Math.min(maxAmount, amount);
        if (!simulate) {
            amount -= extracted;
            if (amount <= 1.0E-4) {
                amount = 0;
                gas = null;
            }
        }
        return extracted;
    }

    public void save(ValueOutput output) {
        output.putDouble(TAG_AMOUNT, amount);
        if (gas instanceof FluidGas fluidGas) {
            output.putString(TAG_GAS, "fluid:" + BuiltInRegistries.FLUID.getKey(fluidGas.getFluid()));
        } else if (gas != null) {
            Identifier id = Gases.REGISTRY.getKey(gas);
            if (id != null) {
                output.putString(TAG_GAS, id.toString());
            }
        }
    }

    public void load(ValueInput input) {
        amount = input.getDoubleOr(TAG_AMOUNT, 0.0);
        gas = null;
        String gasId = input.getStringOr(TAG_GAS, "");
        if (gasId.startsWith("fluid:")) {
            Identifier fluidId = Identifier.tryParse(gasId.substring("fluid:".length()));
            if (fluidId != null) {
                Fluid fluid = BuiltInRegistries.FLUID.getValue(fluidId);
                if (fluid != null && fluid != Fluids.EMPTY) {
                    gas = FluidGases.forFluid(fluid);
                }
            }
            return;
        }
        if (!gasId.isEmpty()) {
            Identifier id = Identifier.tryParse(gasId);
            if (id != null && id.getNamespace().equals(Constants.MOD_ID)) {
                gas = Gases.REGISTRY.getValue(id);
            }
        }
    }
}
