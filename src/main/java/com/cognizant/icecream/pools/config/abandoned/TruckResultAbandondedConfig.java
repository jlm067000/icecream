package com.cognizant.icecream.pools.config.abandoned;

import com.cognizant.icecream.models.Truck;
import com.cognizant.icecream.result.MutableServiceResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TruckResultAbandondedConfig extends DefaultAbandonedConfig<MutableServiceResult<Truck>> {

    TruckResultAbandondedConfig(@Value("${objectPools.serviceResult.maxUnaccessedTime}") int maxUnaccessedTime) {
        super(maxUnaccessedTime);
    }
}
