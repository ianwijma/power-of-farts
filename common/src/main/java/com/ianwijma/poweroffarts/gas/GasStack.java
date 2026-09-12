package com.ianwijma.poweroffarts.gas;

import net.minecraft.resources.Identifier;

import com.ianwijma.poweroffarts.Constants;

public record GasStack(Gas gas, double amount) {

    public static final GasStack EMPTY = new GasStack(null, 0.0);

    public static GasStack of(Gas gas, double amount) {
        return amount <= 0 ? EMPTY : new GasStack(gas, amount);
    }

    public boolean isEmpty() {
        return gas == null || amount <= 0;
    }

    public Identifier id() {
        return Gases.REGISTRY.getKey(gas);
    }

    public GasStack copyWithAmount(double newAmount) {
        return of(gas, newAmount);
    }
}
