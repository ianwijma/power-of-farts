package com.ianwijma.poweroffarts.player;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class PlayerGas {

    public static final String TAG_PENDING = "pendingGas";
    public static final String TAG_STORED = "storedGas";
    public static final String TAG_COOLDOWN = "fartCooldown";

    private static final double FART_THRESHOLD = 0.01;

    private double pendingGas;
    private double storedGas;
    private double excessGas;
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

    public void tickDigestion(double perTick, double capacity, FartListener fartListener) {        tickCooldown();
        if (pendingGas <= 0 && excessGas <= 0) {
            return;
        }
        double moved = Math.min(perTick, pendingGas);
        pendingGas -= moved;
        storedGas += moved;
        double overflow = storedGas - capacity;
        if (overflow > 0) {
            // Body is full: what didn't fit builds up until it demands release
            storedGas = capacity;
            excessGas += overflow;
        }
        if (excessGas >= FART_THRESHOLD && fartListener != null && !isCoolingDown()) {
            double vented = excessGas;
            excessGas = 0;
            fartListener.onFart(vented);
            setFartCooldown(60);
        }
    }

    private double lastSyncedStored = -1;
    private double lastSyncedPending = -1;

    public void syncIfNeeded(net.minecraft.server.level.ServerPlayer player) {
        double roundedStored = Math.round(storedGas * 10.0) / 10.0;
        double roundedPending = Math.round(pendingGas * 10.0) / 10.0;
        if (roundedStored != lastSyncedStored || roundedPending != lastSyncedPending) {
            lastSyncedStored = roundedStored;
            lastSyncedPending = roundedPending;
            com.ianwijma.poweroffarts.platform.Services.NETWORK.sendToPlayer(player,
                    new com.ianwijma.poweroffarts.network.PlayerGasPayload(roundedStored, roundedPending));
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
