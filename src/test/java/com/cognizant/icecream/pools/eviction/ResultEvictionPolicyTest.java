package com.cognizant.icecream.pools.eviction;

import com.cognizant.icecream.result.MutableResult;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ResultEvictionPolicyTest extends EvictionPolicyTest<MutableResult> {

    private ResultEvictionPolicy policy;

    @Before
    public void setup() {

        super.setup();

        policy = new ResultEvictionPolicy(MAX_UNACCESSED_TIME);
    }

    @Test
    public void testEviction() {

        assertFalse(policy.evict(config, recentlyAccessed, 0));
        assertTrue(policy.evict(config, notRecentlyAccessed, 0));
    }
}
