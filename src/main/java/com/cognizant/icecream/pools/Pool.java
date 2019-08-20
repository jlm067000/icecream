package com.cognizant.icecream.pools;

import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.util.HashMap;
import java.util.Map;

abstract class Pool<T> {

    private final int size;
    private final Map<Integer, T> borrowedObjects;
    private final ObjectPool<Integer> keyPool;
    private final ObjectPool<T> objectPool;

    Pool(int size, long maxUnaccessedTime, PooledObjectFactory<T> factory) {

        this.size = size;
        this.borrowedObjects = new HashMap<>();

    }


    private GenericObjectPoolConfig<T> createKeyPoolConfig(long maxWait) {

        GenericObjectPoolConfig<T> config = new GenericObjectPoolConfig<>();
        config.setMaxWaitMillis(maxWait);

        return config;
    }

}
