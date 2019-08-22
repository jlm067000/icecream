package com.cognizant.icecream.pools.factory;

import com.cognizant.icecream.result.MutableServiceResult;
import com.cognizant.icecream.result.ResultFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

abstract class ServiceResultFactory<T> implements PooledObjectFactory<MutableServiceResult<T>> {

    @Override
    public PooledObject<MutableServiceResult<T>> makeObject() {

        MutableServiceResult<T> result = ResultFactory.createMutableServiceResult();
        return new DefaultPooledObject<>(result);
    }

    @Override
    public void destroyObject(PooledObject<MutableServiceResult<T>> p) {

        passivateObject(p);
    }

    @Override
    public boolean validateObject(PooledObject<MutableServiceResult<T>> p) {
        return true;
    }

    @Override
    public void activateObject(PooledObject<MutableServiceResult<T>> p) {}

    @Override
    public void passivateObject(PooledObject<MutableServiceResult<T>> p) {

        p.getObject().setMessage(null);
        p.getObject().setPayload(null);
    }
}
