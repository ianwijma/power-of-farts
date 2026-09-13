package com.ianwijma.poweroffarts.machine;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PipeBlock extends MachineBlock {

    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;

    private static final Map<BlockState, VoxelShape> SHAPE_CACHE = new ConcurrentHashMap<>();

    private static final VoxelShape CORE = box(6, 6, 6, 10, 10, 10);

    public PipeBlock(BlockBehaviour.Properties properties, BlockEntityType.BlockEntitySupplier<? extends BlockEntity> beFactory) {
        super(properties, beFactory);
        registerDefaultState(defaultBlockState()
                .setValue(NORTH, false).setValue(SOUTH, false).setValue(EAST, false)
                .setValue(WEST, false).setValue(UP, false).setValue(DOWN, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, SOUTH, EAST, WEST, UP, DOWN);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelReader level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = defaultBlockState();
        for (Direction direction : Direction.values()) {
            state = state.setValue(propertyFor(direction), connectsTo(level.getBlockState(pos.relative(direction))));
        }
        return state;
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess tickAccess, BlockPos pos,
            Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        return state.setValue(propertyFor(direction), connectsTo(neighborState));
    }

    @Override
    protected VoxelShape getShape(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_CACHE.computeIfAbsent(state, s -> {
            VoxelShape shape = CORE;
            if (s.getValue(NORTH)) shape = Shapes.or(shape, box(6, 6, 0, 10, 10, 8));
            if (s.getValue(SOUTH)) shape = Shapes.or(shape, box(6, 6, 8, 10, 10, 16));
            if (s.getValue(WEST)) shape = Shapes.or(shape, box(0, 6, 6, 8, 10, 10));
            if (s.getValue(EAST)) shape = Shapes.or(shape, box(8, 6, 6, 16, 10, 10));
            if (s.getValue(UP)) shape = Shapes.or(shape, box(6, 8, 6, 10, 16, 10));
            if (s.getValue(DOWN)) shape = Shapes.or(shape, box(6, 0, 6, 10, 8, 10));
            return shape;
        });
    }

    public static boolean connectsTo(BlockState neighbor) {
        return neighbor.getBlock() instanceof MachineBlock;
    }

    public static BooleanProperty propertyFor(Direction direction) {
        return switch (direction) {
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case EAST -> EAST;
            case WEST -> WEST;
            case UP -> UP;
            case DOWN -> DOWN;
        };
    }
}