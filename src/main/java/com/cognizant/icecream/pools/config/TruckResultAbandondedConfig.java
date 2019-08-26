package com.cognizant.icecream.pools.config;

import com.cognizant.icecream.models.Truck;
import com.cognizant.icecream.result.MutableServiceResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TruckServiceResultAbandondedConfig extends DefaultAbandonedConfig<MutableServiceResult<Truck>> {

    TruckServiceResultAbandondedConfig(@Value("${objectPools.serviceResult.maxUnaccessedTime}") int maxUnaccessedTime) {
        super(maxUnaccessedTime);
    }
}
