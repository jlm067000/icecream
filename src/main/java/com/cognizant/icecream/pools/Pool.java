package com.cognizant.icecream.pools;

import com.cognizant.icecream.pools.api.LocalObjectPool;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public abstract class Pool<T> implements LocalObjectPool<T> {

    private final GenericObjectPool<T> objectPool;
    private final Class<? extends Pool<T>> poolType;

    public Pool(
            PooledObjectFactory<T> factory,
            GenericObjectPoolConfig<T> config,
            AbandonedConfig abandonedConfig,
            Class<? extends Pool<T>> poolType
    ) {
        this.objectPool = new GenericObjectPool<>(factory, config, abandonedConfig);
        this.poolType = poolType;
    }

    @Override
    public T getObject() throws PoolCapacityException, Exception {

        try {
            return objectPool.borrowObject();
        }
        catch(Exception ex) {

            int poolCount = objectPool.getNumIdle() + objectPool.getNumActive();
            int capacity = objectPool.getMaxTotal();

            if(poolCount == capacity) {
                throw new PoolCapacityException(capacity, poolType);
            }
            else {
                throw ex;
            }
        }
    }

    @Override
    public void returnObject(T object) {

        try {
            objectPool.returnObject(object);
        }
        catch(Exception ex) {}
    }
}
