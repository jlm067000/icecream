package com.cognizant.icecream.pools;

public interface PoolKey<T> {

    Class<T> getObjectType();
    int getId();
}
