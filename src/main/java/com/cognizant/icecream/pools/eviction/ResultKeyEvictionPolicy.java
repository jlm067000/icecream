package com.cognizant.icecream.pools.eviction;

import com.cognizant.icecream.pools.PoolKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ResultKeyEvictionPolicy extends UnaccessedTimeEvictionPolicy<PoolKey> {

    ResultKeyEvictionPolicy(@Value("objectPools.serviceResult.maxUnaccessedTime") long maxUnaccessedTime) {
        super(maxUnaccessedTime);
    }
}
