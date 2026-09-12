package com.ianwijma.poweroffarts.neoforge;

import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import com.ianwijma.poweroffarts.energy.SimpleEnergyBuffer;
import com.ianwijma.poweroffarts.machine.FartGeneratorBlockEntity;

/**
 * Exposes the generator's internal buffer as a NeoForge {@link EnergyHandler},
 * following the snapshot-journal pattern AE2 uses for its own adapters.
 */
public class GeneratorEnergyHandler extends SnapshotJournal<Long> implements EnergyHandler {

    private final FartGeneratorBlockEntity generator;

    public GeneratorEnergyHandler(FartGeneratorBlockEntity generator) {
        this.generator = generator;
    }

    private SimpleEnergyBuffer buffer() {
        return generator.getEnergy();
    }

    @Override
    public long getAmountAsLong() {
        return buffer().getEnergyStored();
    }

    @Override
    public long getCapacityAsLong() {
        return buffer().getMaxEnergyStored();
    }

    @Override
    public int insert(int maxAmount, TransactionContext context) {
        long received = buffer().receiveEnergy(maxAmount, true);
        if (received <= 0) {
            return 0;
        }
        updateSnapshots(context);
        buffer().receiveEnergy(received, false);
        generator.setChanged();
        return (int) received;
    }

    @Override
    public int extract(int maxAmount, TransactionContext context) {
        long extracted = buffer().extractEnergy(maxAmount, true);
        if (extracted <= 0) {
            return 0;
        }
        updateSnapshots(context);
        buffer().extractEnergy(extracted, false);
        generator.setChanged();
        return (int) extracted;
    }

    @Override
    protected Long createSnapshot() {
        return buffer().getEnergyStored();
    }

    @Override
    protected void revertToSnapshot(Long snapshot) {
        buffer().setStored(snapshot);
    }
}
