package com.ianwijma.poweroffarts.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

import com.ianwijma.poweroffarts.config.PofConfig;

public class GasTankBlockEntity extends GasMachineBlockEntity {

    public GasTankBlockEntity(BlockPos pos, BlockState state) {
        super(PofBlockEntities.GAS_TANK.get(), pos, state, PofConfig.get().gasTankCapacity);
    }

    @Override
    protected void tickMachine(ServerLevel level) {
        // Outputs gas out of the bottom face only
        pushGas(level, PofConfig.get().pipeTransferRate, Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST);
    }

    @Override
    public boolean canAcceptGasFrom(Direction face) {
        return face != Direction.DOWN;
    }
}
