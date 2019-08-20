package com.cognizant.icecream.pools;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.EvictionConfig;
import org.apache.commons.pool2.impl.EvictionPolicy;

class UnaccessedTimeEvictionPolicy<T> implements EvictionPolicy<T> {

    private final long maxUnaccessedTime;

    UnaccessedTimeEvictionPolicy(long maxUnaccessedTimeMillis) {

        this.maxUnaccessedTime = maxUnaccessedTimeMillis;
    }

    @Override
    public boolean evict(EvictionConfig config, PooledObject<T> underTest, int idleCount) {

        long unaccessed = System.currentTimeMillis() - underTest.getLastBorrowTime();

        if(unaccessed > maxUnaccessedTime) {
            return true;
        }
        else {
            return false;
        }
    }
}
