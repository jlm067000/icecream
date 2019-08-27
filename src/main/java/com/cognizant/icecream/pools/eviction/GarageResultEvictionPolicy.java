package com.cognizant.icecream.pools.eviction;

import com.cognizant.icecream.models.Garage;
import com.cognizant.icecream.result.MutableServiceResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
class GarageResultEvictionPolicy extends UnaccessedTimeEvictionPolicy<MutableServiceResult<Garage>> {

    GarageResultEvictionPolicy(@Value("${objectPools.serviceResult.maxUnaccessedTime}") long maxUnaccessedTime) {
        super(maxUnaccessedTime);
    }
}
