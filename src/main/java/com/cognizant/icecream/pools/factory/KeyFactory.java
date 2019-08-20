package com.cognizant.icecream.pools.factory;

import com.cognizant.icecream.pools.PoolKey;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;


abstract class KeyFactory<T> implements PooledObjectFactory<PoolKey<T>> {

    private int idx;
    private final Class<T> objectType;

    KeyFactory(Class<T> objectType) {

        this.objectType = objectType;
    }

    @Override
    public PooledObject<PoolKey<T>> makeObject() {

        PoolKey<T> key = new PoolKeyObject<>(objectType, idx);
        idx ++;

        return new DefaultPooledObject<>(key);
    }

    @Override
    public void destroyObject(PooledObject<PoolKey<T>> p) {}

    @Override
    public boolean validateObject(PooledObject<PoolKey<T>> p) {
        return true;
    }

    @Override
    public void activateObject(PooledObject<PoolKey<T>> p) {}

    @Override
    public void passivateObject(PooledObject<PoolKey<T>> p) {}

}
