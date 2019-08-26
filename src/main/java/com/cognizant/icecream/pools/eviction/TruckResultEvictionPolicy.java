package com.cognizant.icecream.pools.eviction;

import com.cognizant.icecream.models.Truck;
import com.cognizant.icecream.result.MutableServiceResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TruckServiceResultEvictionPolicy extends UnaccessedTimeEvictionPolicy<MutableServiceResult<Truck>> {

    TruckServiceResultEvictionPolicy(@Value("${objectPools.serviceResult.maxUnaccessedTime}") long maxUnaccessedTime) {
        super(maxUnaccessedTime);
    }
}
