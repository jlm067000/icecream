package com.cognizant.icecream.pools;

import com.cognizant.icecream.mock.MockFactory;
import com.cognizant.icecream.models.Garage;
import com.cognizant.icecream.pools.config.DefaultAbandonedConfig;
import com.cognizant.icecream.result.MutableServiceResult;
import com.cognizant.icecream.result.ResultFactory;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class GarageServiceResultObjectPoolTest {

    private static final String MESSAGE = "";
    private static final Garage PAYLOAD = new Garage();

    private PooledObjectFactory<MutableServiceResult<Garage>> factory;
    private GenericObjectPoolConfig<MutableServiceResult<Garage>> config;
    private DefaultAbandonedConfig<MutableServiceResult<Garage>> abandonedConfig;
    private GarageServiceResultObjectPool pool;

    @Before
    public void setup() {

        factory = Mockito.mock(PooledObjectFactory.class);

        try {
            when(factory.makeObject())
                    .thenReturn(new DefaultPooledObject<>(ResultFactory.createMutableServiceResult()));
        }
        catch (Exception ex) {}

        config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(1);
        config.setMaxWaitMillis(0);
        config.setBlockWhenExhausted(true);

        abandonedConfig = MockFactory.createAbandonedConfig(300000);

        pool = new GarageServiceResultObjectPool(factory, config, abandonedConfig);
    }

    @Test(expected = PoolCapacityException.class)
    public void testCreateBeyondCapacity() throws Exception {

        MutableServiceResult<Garage> result = pool.createResult(true, MESSAGE, PAYLOAD);

        assertNotNull(result);
        assertEquals(true, result.isSuccess());
        assertEquals(MESSAGE, result.getMessage());
        assertEquals(PAYLOAD, result.getPayload());

        pool.createResult(true, MESSAGE, PAYLOAD);
    }

    @Test
    public void testReuseObject() throws Exception {

        MutableServiceResult<Garage> result = pool.createResult(true, MESSAGE, PAYLOAD);

        assertNotNull(result);
        assertEquals(true, result.isSuccess());
        assertEquals(MESSAGE, result.getMessage());
        assertEquals(PAYLOAD, result.getPayload());

        pool.returnObject(result);

        result = pool.createResult(false, MESSAGE, PAYLOAD);
        assertEquals(false, result.isSuccess());
        assertEquals(MESSAGE, result.getMessage());
        assertEquals(PAYLOAD, result.getPayload());
    }

}
