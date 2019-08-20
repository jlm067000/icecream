package com.cognizant.icecream.pools.factory;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class KeyObjectFactory implements PooledObjectFactory<Integer> {

    private int idx;

    @Override
    public PooledObject<Integer> makeObject() {

        Integer key = idx;
        idx ++;

        return new DefaultPooledObject<>(key);
    }

    @Override
    public void destroyObject(PooledObject<Integer> p) {}

    @Override
    public boolean validateObject(PooledObject<Integer> p) {
        return true;
    }

    @Override
    public void activateObject(PooledObject<Integer> p) {

    }

    @Override
    public void passivateObject(PooledObject<Integer> p) {

    }
}
