package com.cognizant.icecream.pools.api;

import com.cognizant.icecream.pools.PoolKey;

import java.util.function.Consumer;
import java.util.function.Function;

public interface LocalObjectPool<T> {

    void processObject(PoolKey<T> key, Consumer<T> objectProcessor);
    <V> V processObject(PoolKey<T> key, Function<T, V> objectProcessor);
}
