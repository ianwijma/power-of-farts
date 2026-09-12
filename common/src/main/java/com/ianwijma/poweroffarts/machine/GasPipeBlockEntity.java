package com.ianwijma.poweroffarts.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

import com.ianwijma.poweroffarts.config.PofConfig;

public class GasPipeBlockEntity extends GasMachineBlockEntity {

    private Direction lastInputFace;
    private int lastInputTick = -1;

    public GasPipeBlockEntity(BlockPos pos, BlockState state) {
        super(PofBlockEntities.PIPE.get(), pos, state, 50.0);
    }

    @Override
    protected void tickMachine(ServerLevel level) {
        double rate = PofConfig.get().pipeTransferRate;
        if (lastInputFace != null && lastInputTick == tickCount) {
            // Push anywhere except back the way the gas came in
            pushGas(level, rate, lastInputFace);
        } else {
            pushGas(level, rate);
        }
    }

    @Override
    public void onGasReceived(Direction face) {
        lastInputFace = face;
        lastInputTick = tickCount;
    }
}
