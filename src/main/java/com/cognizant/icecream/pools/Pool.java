package com.cognizant.icecream.pools;

import com.cognizant.icecream.pools.api.LocalObjectPool;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

abstract class Pool<T, U extends T> implements LocalObjectPool<T> {

    private final Map<PoolKey<T>, U> borrowedObjects;
    private final Map<PoolKey<T>, U> unpooledObjects;
    private final ObjectPool<PoolKey<T>> keyPool;
    private final ObjectPool<U> objectPool;

    Pool(
            PooledObjectFactory<U> factory,
            PooledObjectFactory<PoolKey<T>> keyFactory,
            GenericObjectPoolConfig<U> config,
            GenericObjectPoolConfig<PoolKey<T>> keyConfig
    ) {

        this.borrowedObjects = new HashMap<>();
        this.unpooledObjects = new HashMap<>();

        this.keyPool = new GenericObjectPool<>(keyFactory, keyConfig);
        this.objectPool = new GenericObjectPool<>(factory, config);
    }

    PoolKey<T> reservePooledObject() {

        U reserved = reserveObject();

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

    private U reserveObject() {

        if(objectPool.getNumIdle() == 0) {
            return null;
        }

        U reserved;

        try {
            reserved = objectPool.borrowObject();
        }
        catch (Exception ex) {
            return null;
        }

        return reserved;
    }

    private PoolKey<T> reserveKey(U object) {

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

    PoolKey<T> addUnpooledObject(U object) throws Exception {

        PoolKey<T> key = keyPool.borrowObject();
        unpooledObjects.put(key, object);

        return key;
    }

    public void processObject(PoolKey<T> key, Consumer<T> objectProcessor) {

        T object = retrieveObject(key);

        if(object != null) {
            objectProcessor.accept(object);
        }
    }

    public <V> V processObject(PoolKey<T> key, Function<T, V> objectProcessor) {

        T object = retrieveObject(key);

        if(object == null) {
            return null;
        }
        else {
            return objectProcessor.apply(object);
        }
    }

    U retrieveObject(PoolKey<T> key) {

        U object = borrowedObjects.get(key);

        if(object == null) {
            object = unpooledObjects.get(key);
        }
        else {
            borrowedObjects.remove(key);
            returnObjects(key, object);
        }

        if(object == null) {
            return null;
        }
        else {
            unpooledObjects.remove(key);
        }

        return object;
    }

    private void returnObjects(PoolKey<T> key, U object) {

        try {
            objectPool.returnObject(object);
            keyPool.returnObject(key);
        }
        catch(Exception ex) {}
    }

    boolean updateObject(PoolKey<T> key, Consumer<U> updater) {

        U object = retrieveObject(key);

        if(object == null) {
            return false;
        }

        updater.accept(object);

        return true;
    }
}
