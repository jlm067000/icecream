package com.cognizant.icecream.pools.eviction;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.EvictionConfig;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;

public class EvictionPolicyTest<T> {

    static final int MAX_UNACCESSED_TIME = 300_000;

    PooledObject<T> recentlyAccessed;
    PooledObject<T> notRecentlyAccessed;
    EvictionConfig config;

    public void setup() {
        long currentTime = System.currentTimeMillis();
        long excessivelyOldTime = currentTime - 2*MAX_UNACCESSED_TIME;

        recentlyAccessed = Mockito.mock(PooledObject.class);
        when(recentlyAccessed.getLastBorrowTime()).thenReturn(currentTime);

        notRecentlyAccessed = Mockito.mock(PooledObject.class);
        when(notRecentlyAccessed.getLastBorrowTime()).thenReturn(excessivelyOldTime);

        config = Mockito.mock(EvictionConfig.class);
    }
}
