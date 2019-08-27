package com.cognizant.icecream.pools.eviction;

import com.cognizant.icecream.models.Truck;
import com.cognizant.icecream.result.MutableServiceResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class TruckSetResultEvictionPolicy extends UnaccessedTimeEvictionPolicy<MutableServiceResult<Set<Truck>>> {

    TruckSetResultEvictionPolicy(@Value("${objectPools.serviceResult.maxUnaccessedTime}") long maxUnaccessedTime) {
        super(maxUnaccessedTime);
    }
}
