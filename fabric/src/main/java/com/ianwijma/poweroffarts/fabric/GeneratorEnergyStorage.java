package com.ianwijma.poweroffarts.fabric;

import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import team.reborn.energy.api.EnergyStorage;

import com.ianwijma.poweroffarts.energy.SimpleEnergyBuffer;
import com.ianwijma.poweroffarts.machine.FartGeneratorBlockEntity;

/**
 * Exposes the generator's internal buffer as a TR {@link EnergyStorage}.
 */
public class GeneratorEnergyStorage extends SnapshotParticipant<Long> implements EnergyStorage {

    private final FartGeneratorBlockEntity generator;

    public GeneratorEnergyStorage(FartGeneratorBlockEntity generator) {
        this.generator = generator;
    }

    private SimpleEnergyBuffer buffer() {
        return generator.getEnergy();
    }

    @Override
    public long insert(long maxAmount, net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext transaction) {
        long received = buffer().receiveEnergy(maxAmount, true);
        if (received <= 0) {
            return 0;
        }
        updateSnapshots(transaction);
        buffer().receiveEnergy(received, false);
        generator.setChanged();
        return received;
    }

    @Override
    public long extract(long maxAmount, net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext transaction) {
        long extracted = buffer().extractEnergy(maxAmount, true);
        if (extracted <= 0) {
            return 0;
        }
        updateSnapshots(transaction);
        buffer().extractEnergy(extracted, false);
        generator.setChanged();
        return extracted;
    }

    @Override
    public long getAmount() {
        return buffer().getEnergyStored();
    }

    @Override
    public long getCapacity() {
        return buffer().getMaxEnergyStored();
    }

    @Override
    protected Long createSnapshot() {
        return buffer().getEnergyStored();
    }

    @Override
    protected void readSnapshot(Long snapshot) {
        buffer().setStored(snapshot);
    }
}
