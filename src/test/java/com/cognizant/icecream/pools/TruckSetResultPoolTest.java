package com.cognizant.icecream.pools;

import com.cognizant.icecream.models.Truck;
import com.cognizant.icecream.result.MutableServiceResult;
import com.cognizant.icecream.result.ResultFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TruckSetResultPoolTest extends PoolTest<MutableServiceResult<Set<Truck>>> {

    private static final String MESSAGE = "";
    private static final Set<Truck> PAYLOAD = new HashSet<>();

    private TruckSetResultPool pool;

    @Before
    public void setup() {

        super.setup(ResultFactory.createMutableServiceResult());
        pool = new TruckSetResultPool(factory, config, abandonedConfig);
    }

    @Test(expected = PoolCapacityException.class)
    public void testCreateBeyondCapacity() throws Exception {

        MutableServiceResult<Set<Truck>> result = pool.createResult(true, MESSAGE, PAYLOAD);

        assertNotNull(result);
        assertEquals(true, result.isSuccess());
        assertEquals(MESSAGE, result.getMessage());
        assertEquals(PAYLOAD, result.getPayload());

        pool.createResult(true, MESSAGE, PAYLOAD);
    }

    @Test
    public void testReuseObject() throws Exception {

        MutableServiceResult<Set<Truck>> result = pool.createResult(true, MESSAGE, PAYLOAD);

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
