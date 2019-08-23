package com.cognizant.icecream.pools.eviction;

import com.cognizant.icecream.models.Garage;
import com.cognizant.icecream.result.MutableServiceResult;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.EvictionConfig;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class GarageServiceResultEvictionPolicyTest {

    private static final int MAX_UNACCESSED_TIME = 300_000;

    private static PooledObject<MutableServiceResult<Garage>> recentlyAccessed;
    private static PooledObject<MutableServiceResult<Garage>> notRecentlyAccessed;
    private static EvictionConfig config;

    private GarageServiceResultEvictionPolicy policy;

    @BeforeClass
    public static void init() {

        long currentTime = System.currentTimeMillis();
        long excessivelyOldTime = currentTime - 2*MAX_UNACCESSED_TIME;

        recentlyAccessed = Mockito.mock(PooledObject.class);
        when(recentlyAccessed.getLastBorrowTime()).thenReturn(currentTime);

        notRecentlyAccessed = Mockito.mock(PooledObject.class);
        when(notRecentlyAccessed.getLastBorrowTime()).thenReturn(excessivelyOldTime);

        config = Mockito.mock(EvictionConfig.class);
    }

    @Before
    public void setup() {
        policy = new GarageServiceResultEvictionPolicy(MAX_UNACCESSED_TIME);
    }

    @Test
    public void testEviction() {

        assertFalse(policy.evict(config, recentlyAccessed, 0));
        assertTrue(policy.evict(config, notRecentlyAccessed, 0));
    }

}
