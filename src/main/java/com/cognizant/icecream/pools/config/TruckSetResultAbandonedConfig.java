package com.cognizant.icecream.pools.config;

import com.cognizant.icecream.models.Truck;
import com.cognizant.icecream.result.MutableServiceResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class TruckSetResultAbandonedConfig extends DefaultAbandonedConfig<MutableServiceResult<Set<Truck>>> {

    TruckSetResultAbandonedConfig(@Value("${objectPools.serviceResult.maxUnaccessedTime}") int maxUnaccessedTime) {
        super(maxUnaccessedTime);
    }
}
