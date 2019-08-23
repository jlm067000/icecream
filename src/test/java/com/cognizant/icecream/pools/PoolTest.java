package com.cognizant.icecream.pools;

import com.cognizant.icecream.mock.MockFactory;
import com.cognizant.icecream.pools.config.DefaultAbandonedConfig;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;

public class PoolTest<T> {

    PooledObjectFactory<T> factory;
    GenericObjectPoolConfig<T> config;
    DefaultAbandonedConfig<T> abandonedConfig;


    public void setup(T object) {

        factory = Mockito.mock(PooledObjectFactory.class);

        try {
            when(factory.makeObject()).thenReturn(new DefaultPooledObject<>(object));
        }
        catch (Exception ex) {}

        config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(1);
        config.setMaxWaitMillis(0);
        config.setBlockWhenExhausted(true);

        abandonedConfig = MockFactory.createAbandonedConfig(300000);
    }

}
