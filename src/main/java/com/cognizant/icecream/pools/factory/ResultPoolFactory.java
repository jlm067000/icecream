package com.cognizant.icecream.pools.factory;

import com.cognizant.icecream.result.MutableResult;
import com.cognizant.icecream.result.ResultFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.springframework.stereotype.Component;

@Component
public class ResultPoolFactory implements PooledObjectFactory<MutableResult> {

    @Override
    public PooledObject<MutableResult> makeObject() {

        MutableResult result = ResultFactory.createMutableResult();
        return new DefaultPooledObject<>(result);
    }

    @Override
    public void destroyObject(PooledObject<MutableResult> p) {

        passivateObject(p);
    }

    @Override
    public boolean validateObject(PooledObject<MutableResult> p) {
        return false;
    }

    @Override
    public void activateObject(PooledObject<MutableResult> p) {}

    @Override
    public void passivateObject(PooledObject<MutableResult> p) {

        MutableResult result = p.getObject();
        result.setMessage(null);
    }
}
