package com.cognizant.icecream.pools.factory;

import com.cognizant.icecream.models.Truck;
import com.cognizant.icecream.result.MutableServiceResult;
import org.apache.commons.pool2.PooledObject;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class TruckResultFactoryTest {

    private static TruckResultFactory factory;

    @BeforeClass
    public static void init() {
        factory = new TruckResultFactory();
    }

    @Test
    public void testMakeObject() {

        PooledObject<MutableServiceResult<Truck>> object = factory.makeObject();

        assertNotNull(object);
        assertNotNull(object.getObject());
    }
}
