package com.ianwijma.poweroffarts.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

import com.ianwijma.poweroffarts.config.PofConfig;
import com.ianwijma.poweroffarts.energy.SimpleEnergyBuffer;
import com.ianwijma.poweroffarts.platform.Services;

public class FartGeneratorBlockEntity extends GasMachineBlockEntity {

    public static final long ENERGY_CAPACITY = 10_000;
    public static final long MAX_EXTRACT = 200;

    private final SimpleEnergyBuffer energy = new SimpleEnergyBuffer(ENERGY_CAPACITY, MAX_EXTRACT, MAX_EXTRACT);

    public FartGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(PofBlockEntities.GENERATOR.get(), pos, state, 200.0);
    }

    public SimpleEnergyBuffer getEnergy() {
        return energy;
    }

    @Override
    protected void tickMachine(ServerLevel level) {
        PofConfig config = PofConfig.get();
        double available = tank.getAmount();
        if (available > 0 && energy.getEnergyStored() < ENERGY_CAPACITY) {
            double burned = tank.extract(Math.min(config.generatorBurnRate, available), false);
            energy.receiveEnergy(Math.round(burned * config.generatorPowerOutput / config.generatorBurnRate), false);
        }
        long toPush = Math.min(MAX_EXTRACT, energy.getEnergyStored());
        if (toPush > 0) {
            for (Direction direction : Direction.values()) {
                long pushed = Services.PLATFORM.pushEnergy(level, worldPosition, direction, toPush);
                if (pushed > 0) {
                    energy.extractEnergy(pushed, false);
                    toPush -= pushed;
                    if (toPush <= 0) {
                        break;
                    }
                }
            }
        }
    }

    @Override
    public boolean canAcceptGasFrom(Direction face) {
        return true;
    }
}
