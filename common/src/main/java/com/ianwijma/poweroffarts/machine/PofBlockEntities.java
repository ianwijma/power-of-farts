package com.ianwijma.poweroffarts.machine;

import java.util.Set;
import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import com.ianwijma.poweroffarts.platform.Services;

public final class PofBlockEntities {

    public static final Supplier<BlockEntityType<GasDepositorBlockEntity>> DEPOSITOR =
            register("gas_depositor", GasDepositorBlockEntity::new, PofBlocks.DEPOSITOR);
    public static final Supplier<BlockEntityType<GasPipeBlockEntity>> PIPE =
            register("gas_pipe", GasPipeBlockEntity::new, PofBlocks.PIPE);
    public static final Supplier<BlockEntityType<GasTankBlockEntity>> GAS_TANK =
            register("gas_tank", GasTankBlockEntity::new, PofBlocks.GAS_TANK);
    public static final Supplier<BlockEntityType<FartGeneratorBlockEntity>> GENERATOR =
            register("fart_generator", FartGeneratorBlockEntity::new, PofBlocks.GENERATOR);

    private static <T extends BlockEntity> Supplier<BlockEntityType<T>> register(
            String name, BlockEntityType.BlockEntitySupplier<? extends T> factory, Supplier<Block> block) {
        return Services.REGISTRY.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, name, id ->
                new BlockEntityType<>(factory, Set.of(block.get())));
    }

    public static void touch() {
    }
}
