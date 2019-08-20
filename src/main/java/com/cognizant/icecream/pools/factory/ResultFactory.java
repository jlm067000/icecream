package com.cognizant.icecream.pools.factory;

import com.cognizant.icecream.models.Garage;
import com.cognizant.icecream.models.result.MutableServiceResult;
import com.cognizant.icecream.pools.ServiceResultObject;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.springframework.stereotype.Component;

@Component
class ResultFactory implements PooledObjectFactory<MutableServiceResult> {


    @Override
    public PooledObject<MutableServiceResult> makeObject() {

        MutableServiceResult<Garage> result = new ServiceResultObject<>();

        return new DefaultPooledObject<>(result);
    }

    @Override
    public void destroyObject(PooledObject<MutableServiceResult> p) {

        passivateObject(p);
    }

    @Override
    public boolean validateObject(PooledObject<MutableServiceResult> p) {
        return true;
    }

    @Override
    public void activateObject(PooledObject<MutableServiceResult> p) {}

    @Override
    public void passivateObject(PooledObject<MutableServiceResult> p) {

        p.getObject().setMessage(null);
        p.getObject().setPayload(null);
    }
}
