package com.ianwijma.poweroffarts.fabric;

import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

import com.ianwijma.poweroffarts.Constants;
import com.ianwijma.poweroffarts.platform.IPlatformRegistry;

public class FabricRegistryService implements IPlatformRegistry {

    @Override
    public <T, V extends T> Supplier<V> register(Registry<T> registry, String name, Function<Identifier, V> factory) {
        Identifier id = Identifier.fromNamespaceAndPath(Constants.MOD_ID, name);
        V value = factory.apply(id);
        Registry.register(registry, id, value);
        return () -> value;
    }
}
