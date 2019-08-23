package com.cognizant.icecream.pools;

import com.cognizant.icecream.result.MutableResult;
import com.cognizant.icecream.result.ResultFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ResultPoolTest extends PoolTest<MutableResult> {

    private static final String MESSAGE = "";
    private ResultObjectPool pool;

    @Before
    public void setup() {

        super.setup(ResultFactory.createMutableResult());
        pool = new ResultObjectPool(factory, config, abandonedConfig);
    }

    @Test(expected = PoolCapacityException.class)
    public void testCreateBeyondCapacity() throws Exception {

        MutableResult result = pool.createResult(true, MESSAGE);

        assertNotNull(result);
        assertEquals(true, result.isSuccess());
        assertEquals(MESSAGE, result.getMessage());

        pool.createResult(true, MESSAGE);
    }

    @Test
    public void testReuseObject() throws Exception {

        MutableResult result = pool.createResult(true, MESSAGE);

        assertNotNull(result);
        assertEquals(true, result.isSuccess());
        assertEquals(MESSAGE, result.getMessage());

        pool.returnObject(result);

        result = pool.createResult(false, MESSAGE);
        assertEquals(false, result.isSuccess());
        assertEquals(MESSAGE, result.getMessage());
    }
}
