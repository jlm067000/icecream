package com.cognizant.icecream.pools.factory;

import com.cognizant.icecream.models.Truck;
import com.cognizant.icecream.result.MutableServiceResult;
import com.cognizant.icecream.result.ResultFactory;
import com.cognizant.icecream.result.ServiceResult;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TruckSetResultFactoryTest {

    private static TruckSetResultFactory factory;

    private PooledObject<MutableServiceResult<Set<Truck>>> object;

    @BeforeClass
    public static void init() {
        factory = new TruckSetResultFactory();
    }

    @Before
    public void setup() {

        Set<Truck> truckSet = new HashSet<>();
        MutableServiceResult<Set<Truck>> result = ResultFactory.createMutableServiceResult(true, "", truckSet);

        object = new DefaultPooledObject<>(result);
    }

    @Test
    public void testMakeObject() {

        PooledObject<MutableServiceResult<Set<Truck>>> object = factory.makeObject();

        assertNotNull(object);
        assertNotNull(object.getObject());
    }

    @Test
    public void testPassivateObject() {

        factory.passivateObject(object);

        ServiceResult<Set<Truck>> result = object.getObject();

        assertNotNull(result);
        assertNull(result.getMessage());
        assertNull(result.getPayload());
    }
}
