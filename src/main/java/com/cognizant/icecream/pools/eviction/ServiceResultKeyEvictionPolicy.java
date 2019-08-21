package com.cognizant.icecream.pools.eviction;

import com.cognizant.icecream.pools.PoolKey;
import com.cognizant.icecream.result.ServiceResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServiceResultKeyEvictionPolicy extends UnaccessedTimeEvictionPolicy<PoolKey<ServiceResult>> {

    ServiceResultKeyEvictionPolicy(@Value("${objectPools.serviceResult.maxUnaccessedTime}") long maxUnaccessedTime) {
        super(maxUnaccessedTime);
    }
}
