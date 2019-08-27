package com.cognizant.icecream.pools.factory;

import com.cognizant.icecream.models.Invoice;
import com.cognizant.icecream.result.MutableServiceResult;
import org.apache.commons.pool2.PooledObject;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class InvoiceResultFactoryTest {

    private static InvoiceResultFactory factory;

    @BeforeClass
    public static void init() {
        factory = new InvoiceResultFactory();
    }

    @Test
    public void testMakeObject() {

        PooledObject<MutableServiceResult<Invoice>> object = factory.makeObject();

        assertNotNull(object);
        assertNotNull(object.getObject());
    }
}
