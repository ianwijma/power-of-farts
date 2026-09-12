package com.ianwijma.poweroffarts.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import com.ianwijma.poweroffarts.config.PofConfig;
import com.ianwijma.poweroffarts.gas.Gas;
import com.ianwijma.poweroffarts.gas.PofGases;
import com.ianwijma.poweroffarts.item.GasBagItem;

public class GasDepositorBlockEntity extends GasMachineBlockEntity {

    private ItemStack bag = ItemStack.EMPTY;

    public GasDepositorBlockEntity(BlockPos pos, BlockState state) {
        super(PofBlockEntities.DEPOSITOR.get(), pos, state, 100.0);
    }

    public ItemStack getBag() {
        return bag;
    }

    public void setBag(ItemStack stack) {
        bag = stack;
        setChanged();
    }

    @Override
    protected void tickMachine(ServerLevel level) {
        PofConfig config = PofConfig.get();
        if (!bag.isEmpty() && bag.getItem() instanceof GasBagItem) {
            double bagGas = GasBagItem.getGas(bag);
            if (bagGas > 0) {
                double drained = Math.min(config.depositorTransferRate, bagGas);
                double accepted = tank.insert(PofGases.FARTS.get(), drained, false);
                if (accepted > 0) {
                    GasBagItem.setGas(bag, bagGas - accepted);
                    if (GasBagItem.getGas(bag) <= 0) {
                        bag = ItemStack.EMPTY;
                    }
                    setChanged();
                }
            }
        }
        pushGas(level, config.depositorTransferRate);
    }

    @Override
    public boolean canAcceptGasFrom(Direction face) {
        return false;
    }

    @Override
    public double insertGas(Gas gas, double maxAmount, Direction from, boolean simulate) {
        return 0;
    }
}
