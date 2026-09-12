package com.ianwijma.poweroffarts.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;

import org.jspecify.annotations.Nullable;

public class MachineBlock extends Block implements EntityBlock {

    private final BlockEntityType.BlockEntitySupplier<? extends BlockEntity> beFactory;

    public MachineBlock(BlockBehaviour.Properties properties, BlockEntityType.BlockEntitySupplier<? extends BlockEntity> beFactory) {
        super(properties);
        this.beFactory = beFactory;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return beFactory.create(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level instanceof ServerLevel) {
            return (lvl, pos, blockState, be) -> {
                if (be instanceof GasMachineBlockEntity machine) {
                    machine.serverTick((ServerLevel) lvl);
                }
            };
        }
        return null;
    }
}
