package com.cognizant.icecream.pools.factory;

import com.cognizant.icecream.result.MutableResult;
import org.apache.commons.pool2.PooledObject;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class ResultPoolFactoryTest {

    private static ResultPoolFactory factory;

    @BeforeClass
    public static void init() {
        factory = new ResultPoolFactory();
    }

    @Test
    public void testMakeObject() {

        PooledObject<MutableResult> object = factory.makeObject();

        assertNotNull(object);
        assertNotNull(object.getObject());
    }
}
