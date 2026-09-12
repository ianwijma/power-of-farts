package com.ianwijma.poweroffarts.player;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class PlayerGas {

    public static final String TAG_PENDING = "pendingGas";
    public static final String TAG_STORED = "storedGas";
    public static final String TAG_COOLDOWN = "fartCooldown";

    private double pendingGas;
    private double storedGas;
    private int fartCooldownTicks;

    public double getPendingGas() {
        return pendingGas;
    }

    public double getStoredGas() {
        return storedGas;
    }

    public void addPendingGas(double amount) {
        pendingGas += Math.max(0, amount);
    }

    public boolean isCoolingDown() {
        return fartCooldownTicks > 0;
    }

    public void setFartCooldown(int ticks) {
        fartCooldownTicks = ticks;
    }

    public void tickCooldown() {
        if (fartCooldownTicks > 0) {
            fartCooldownTicks--;
        }
    }

    public double ventExcess(double capacity) {
        if (storedGas <= capacity) {
            return 0;
        }
        double vented = storedGas - capacity;
        storedGas = capacity;
        return vented;
    }

    public double drainStored(double maxAmount) {
        double drained = Math.min(storedGas, maxAmount);
        storedGas -= drained;
        return drained;
    }

    public double fillStored(double amount, double capacity) {
        double filled = Math.min(Math.max(0, amount), capacity - storedGas);
        storedGas += filled;
        return filled;
    }

    public void tickDigestion(double perTick, double capacity, FartListener fartListener) {
        tickCooldown();
        if (pendingGas <= 0) {
            return;
        }
        double moved = Math.min(perTick, pendingGas);
        pendingGas -= moved;
        storedGas += moved;
        double vented = ventExcess(capacity);
        if (vented > 0 && fartListener != null && !isCoolingDown()) {
            fartListener.onFart(vented);
            setFartCooldown(60);
        }
    }

    public void save(ValueOutput output) {
        output.putDouble(TAG_PENDING, pendingGas);
        output.putDouble(TAG_STORED, storedGas);
        output.putInt(TAG_COOLDOWN, fartCooldownTicks);
    }

    public void read(ValueInput input) {
        pendingGas = input.getDoubleOr(TAG_PENDING, 0.0);
        storedGas = input.getDoubleOr(TAG_STORED, 0.0);
        fartCooldownTicks = input.getIntOr(TAG_COOLDOWN, 0);
    }
}
