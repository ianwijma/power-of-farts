package com.ianwijma.poweroffarts.platform;

import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

import java.util.function.Function;
import java.util.function.Supplier;

public interface IPlatformRegistry {

    <T, V extends T> Supplier<V> register(Registry<T> registry, String name, Function<Identifier, V> factory);

    default void commit() {
    }
}
