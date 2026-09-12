package com.ianwijma.poweroffarts.neoforge;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import com.ianwijma.poweroffarts.Constants;
import com.ianwijma.poweroffarts.platform.IPlatformRegistry;

public class NeoForgeRegistryService implements IPlatformRegistry {

    private static final NeoForgeRegistryService INSTANCE = new NeoForgeRegistryService();

    private final Map<Registry<?>, DeferredRegister<?>> registers = new HashMap<>();
    private IEventBus modEventBus;
    private boolean committed;

    public static NeoForgeRegistryService instance() {
        return INSTANCE;
    }

    public static void init(IEventBus modEventBus) {
        INSTANCE.modEventBus = modEventBus;
    }

    public void commit() {
        if (committed) {
            return;
        }
        if (modEventBus == null) {
            throw new IllegalStateException("NeoForgeRegistryService.init must be called before commit");
        }
        committed = true;
        registers.values().forEach(register -> register.register(modEventBus));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T, V extends T> Supplier<V> register(Registry<T> registry, String name, Function<Identifier, V> factory) {
        DeferredRegister<T> deferredRegister = (DeferredRegister<T>) registers.computeIfAbsent(
                registry, key -> DeferredRegister.create(key.key(), Constants.MOD_ID));
        return deferredRegister.register(name, factory);
    }
}
