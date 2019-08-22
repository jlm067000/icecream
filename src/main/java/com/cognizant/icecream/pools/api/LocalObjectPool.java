package com.cognizant.icecream.pools.api;

import com.cognizant.icecream.pools.PoolCapacityException;

public interface LocalObjectPool<T> {

    T getObject() throws PoolCapacityException, Exception;
    void returnObject(T object);
}
