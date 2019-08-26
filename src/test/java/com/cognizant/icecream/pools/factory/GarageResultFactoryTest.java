package com.cognizant.icecream.pools.factory;

import com.cognizant.icecream.models.Garage;
import com.cognizant.icecream.result.MutableServiceResult;
import org.apache.commons.pool2.PooledObject;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class GarageServiceResultFactoryTest {

    private static GarageResultFactory factory;

    @BeforeClass
    public static void init() {
        factory = new GarageResultFactory();
    }

    @Test
    public void testMakeObject() {

        PooledObject<MutableServiceResult<Garage>> object = factory.makeObject();

        assertNotNull(object);
        assertNotNull(object.getObject());
    }
}
