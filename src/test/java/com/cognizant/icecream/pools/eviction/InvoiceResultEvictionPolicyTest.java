package com.cognizant.icecream.pools.eviction;

import com.cognizant.icecream.models.Invoice;
import com.cognizant.icecream.result.MutableServiceResult;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InvoiceResultEvictionPolicyTest extends EvictionPolicyTest<MutableServiceResult<Invoice>> {

    private InvoiceResultEvictionPolicy policy;

    @Before
    public void setup() {

        super.setup();

        policy = new InvoiceResultEvictionPolicy(MAX_UNACCESSED_TIME);
    }

    @Test
    public void testEviction() {

        assertFalse(policy.evict(config, recentlyAccessed, 0));
        assertTrue(policy.evict(config, notRecentlyAccessed, 0));
    }
}
