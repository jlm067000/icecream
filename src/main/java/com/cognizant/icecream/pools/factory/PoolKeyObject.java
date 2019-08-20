package com.cognizant.icecream.pools.factory;

import com.cognizant.icecream.pools.PoolKey;

class PoolKeyObject<T> implements PoolKey<T> {

    private int id;
    private Class<T> objectType;

    PoolKeyObject(Class<T> objectType, int id) {

        this.objectType = objectType;
        this.id = id;
    }

    @Override
    public Class<T> getObjectType() {
        return objectType;
    }

    @Override
    public int getId() {
        return id;
    }
}
