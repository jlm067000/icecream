package com.cognizant.icecream.pools;

import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.EvictionPolicy;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

abstract class Pool<T> {

    private final int size;
    private final Map<PoolKey<T>, T> borrowedObjects;
    private final Map<PoolKey<T>, T> unpooledObjects;
    private final ObjectPool<PoolKey<T>> keyPool;
    private final ObjectPool<T> objectPool;

    Pool(
            int size,
            PooledObjectFactory<T> factory,
            PooledObjectFactory<PoolKey<T>> keyFactory,
            EvictionPolicy<T> evictionPolicy,
            EvictionPolicy<PoolKey<T>> keyEvictionPolicy
    ) {

        this.size = size;
        this.borrowedObjects = new HashMap<>();
        this.unpooledObjects = new HashMap<>();

        this.keyPool = new GenericObjectPool<>(keyFactory, createKeyPoolConfig(keyEvictionPolicy));
        this.objectPool = new GenericObjectPool<>(factory, createPoolConfig(evictionPolicy));
    }


    private GenericObjectPoolConfig<PoolKey<T>> createKeyPoolConfig(EvictionPolicy<PoolKey<T>> evictionPolicy) {

        GenericObjectPoolConfig<PoolKey<T>> config = new GenericObjectPoolConfig<>();
        config.setEvictionPolicy(evictionPolicy);

        return config;
    }

    private GenericObjectPoolConfig<T> createPoolConfig(EvictionPolicy<T> evictionPolicy) {

        GenericObjectPoolConfig<T> config = new GenericObjectPoolConfig<>();
        config.setEvictionPolicy(evictionPolicy);
        config.setMaxTotal(size);

        return config;
    }

    PoolKey<T> reservePooledObject() {

        T reserved = reserveObject();

        if(reserved == null) {
            return null;
        }

        PoolKey<T> key = reserveKey(reserved);

        if(key == null) {
            return null;
        }

        borrowedObjects.put(key, reserved);

        return key;
    }

    private T reserveObject() {

        if(objectPool.getNumIdle() == 0) {
            return null;
        }

        T reserved;

        try {
            reserved = objectPool.borrowObject();
        }
        catch (Exception ex) {
            return null;
        }

        return reserved;
    }

    private PoolKey<T> reserveKey(T object) {

        PoolKey<T> key;

        try {
            key = keyPool.borrowObject();
        }
        catch (Exception ex1) {

            try {
                objectPool.returnObject(object);
            }
            catch (Exception ex2) {}
            finally {
                return null;
            }
        }

        return key;
    }

    PoolKey<T> addUnpooledObject(T object) throws Exception {

        PoolKey<T> key = keyPool.borrowObject();
        unpooledObjects.put(key, object);

        return key;
    }

    void processObject(PoolKey<T> key, Consumer<T> objectProcessor) {

        T object = retrieveObject(key);

        if(object != null) {
            objectProcessor.accept(object);
        }
    }

    <U> U processObject(PoolKey<T> key, Function<T, U> objectProcessor) {

        T object = retrieveObject(key);

        if(object == null) {
            return null;
        }
        else {
            return objectProcessor.apply(object);
        }
    }

    T retrieveObject(PoolKey<T> key) {

        T object = borrowedObjects.get(key);

        if(object == null) {
            object = unpooledObjects.get(key);
        }
        else {
            borrowedObjects.remove(key);
        }

        if(object == null) {
            return null;
        }
        else {
            unpooledObjects.remove(key);
        }

        return object;
    }

    boolean updateObject(PoolKey<T> key, Consumer<T> updater) {

        T object = retrieveObject(key);

        if(object == null) {
            return false;
        }

        updater.accept(object);

        return true;
    }

}
