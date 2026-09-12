package com.ianwijma.poweroffarts.machine;

import java.util.function.Supplier;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import com.ianwijma.poweroffarts.platform.Services;

public final class PofBlocks {

    public static final Supplier<Block> DEPOSITOR = registerMachine("gas_depositor", GasDepositorBlockEntity::new);
    public static final Supplier<Block> PIPE = registerMachine("gas_pipe", GasPipeBlockEntity::new);
    public static final Supplier<Block> GAS_TANK = registerMachine("gas_tank", GasTankBlockEntity::new);
    public static final Supplier<Block> GENERATOR = registerMachine("fart_generator", FartGeneratorBlockEntity::new);

    private static Supplier<Block> registerMachine(String name, BlockEntityType.BlockEntitySupplier<? extends BlockEntity> beFactory) {
        Supplier<Block> block = Services.REGISTRY.register(BuiltInRegistries.BLOCK, name, id ->
                new MachineBlock(BlockBehaviour.Properties.of().strength(1.5F)
                        .setId(ResourceKey.create(Registries.BLOCK, id)), beFactory));
        Services.REGISTRY.register(BuiltInRegistries.ITEM, name, itemId ->
                new BlockItem(block.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, itemId))));
        return block;
    }

    public static void touch() {
    }
}
