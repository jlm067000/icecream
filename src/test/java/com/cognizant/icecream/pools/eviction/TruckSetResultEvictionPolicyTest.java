package com.cognizant.icecream.pools.eviction;

import com.cognizant.icecream.models.Truck;
import com.cognizant.icecream.result.MutableServiceResult;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TruckSetResultEvictionPolicyTest extends EvictionPolicyTest<MutableServiceResult<Set<Truck>>> {

    private TruckSetResultEvictionPolicy policy;

    @Before
    public void setup() {

        super.setup();

        policy = new TruckSetResultEvictionPolicy(MAX_UNACCESSED_TIME);
    }

    @Test
    public void testEviction() {

        assertFalse(policy.evict(config, recentlyAccessed, 0));
        assertTrue(policy.evict(config, notRecentlyAccessed, 0));
    }
}
