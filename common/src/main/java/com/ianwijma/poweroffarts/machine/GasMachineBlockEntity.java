package com.ianwijma.poweroffarts.machine;

import org.jspecify.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import com.ianwijma.poweroffarts.gas.Gas;
import com.ianwijma.poweroffarts.gas.GasTank;
import com.ianwijma.poweroffarts.platform.Services;

public abstract class GasMachineBlockEntity extends BlockEntity implements IGasAcceptor {

    protected final GasTank tank;
    protected int tickCount;

    protected GasMachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, double capacity) {
        super(type, pos, state);
        this.tank = new GasTank(capacity);
    }

    public GasTank getTank() {
        return tank;
    }

    public void serverTick(ServerLevel level) {
        tickCount++;
        tickMachine(level);
    }

    protected abstract void tickMachine(ServerLevel level);

    protected void pushGas(ServerLevel level, double maxAmount, Direction... skipFaces) {
        double remaining = maxAmount;
        if (remaining <= 0 || tank.isEmpty()) {
            return;
        }
        for (Direction direction : Direction.values()) {
            if (remaining <= 0) {
                return;
            }
            boolean skipped = false;
            for (Direction skipFace : skipFaces) {
                if (skipFace == direction) {
                    skipped = true;
                    break;
                }
            }
            if (skipped) {
                continue;
            }
            double offered = Math.min(remaining, tank.getAmount());
            if (offered <= 0) {
                return;
            }
            double pushed = insertIntoNeighbor(level, worldPosition.relative(direction), direction.getOpposite(), tank.getGas(), offered);
            if (pushed <= 0) {
                // Not one of our machines: try pushing out to foreign fluid tanks
                pushed = Services.PLATFORM.pushGasToFluidTank(level, worldPosition.relative(direction), direction.getOpposite(), tank.getGas(), offered);
            }
            if (pushed > 0) {
                tank.extract(pushed, false);
                remaining -= pushed;
            }
        }
    }

    public static double insertIntoNeighbor(ServerLevel level, BlockPos pos, Direction face, Gas gas, double amount) {
        if (level.getBlockEntity(pos) instanceof IGasAcceptor acceptor && acceptor.canAcceptGasFrom(face)) {
            double accepted = acceptor.insertGas(gas, amount, face, false);
            if (accepted > 0) {
                acceptor.onGasReceived(face);
            }
            return accepted;
        }
        return 0;
    }

    @Override
    public boolean canAcceptGasFrom(Direction face) {
        return true;
    }

    @Override
    public double insertGas(Gas gas, double maxAmount, Direction from, boolean simulate) {
        return tank.insert(gas, maxAmount, simulate);
    }

    @Override
    public void onGasReceived(Direction face) {
    }
}
