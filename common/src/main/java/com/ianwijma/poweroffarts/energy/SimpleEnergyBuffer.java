package com.ianwijma.poweroffarts.energy;

public class SimpleEnergyBuffer {

    private final long capacity;
    private final long maxReceive;
    private final long maxExtract;
    private long stored;

    public SimpleEnergyBuffer(long capacity, long maxReceive, long maxExtract) {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
    }

    public long receiveEnergy(long toReceive, boolean simulate) {
        if (toReceive <= 0) {
            return 0;
        }
        long received = Math.min(capacity - stored, Math.min(maxReceive, toReceive));
        if (!simulate) {
            stored += received;
        }
        return received;
    }

    public long extractEnergy(long toExtract, boolean simulate) {
        if (toExtract <= 0) {
            return 0;
        }
        long extracted = Math.min(stored, Math.min(maxExtract, toExtract));
        if (!simulate) {
            stored -= extracted;
        }
        return extracted;
    }

    public long getEnergyStored() {
        return stored;
    }

    public void setStored(long amount) {
        stored = Math.max(0, Math.min(capacity, amount));
    }

    public long getMaxEnergyStored() {
        return capacity;
    }

    public long getMaxReceive() {
        return maxReceive;
    }

    public long getMaxExtract() {
        return maxExtract;
    }

    public boolean canExtract() {
        return maxExtract > 0;
    }

    public boolean canReceive() {
        return maxReceive > 0;
    }
}
