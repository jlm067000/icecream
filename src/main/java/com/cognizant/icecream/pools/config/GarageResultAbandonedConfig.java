package com.cognizant.icecream.pools.config;

import com.cognizant.icecream.models.Garage;
import com.cognizant.icecream.result.MutableServiceResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
class GarageResultAbandonedConfig extends DefaultAbandonedConfig<MutableServiceResult<Garage>> {

    GarageResultAbandonedConfig(@Value("${objectPools.serviceResult.maxUnaccessedTime}") int maxUnaccessedTime) {
        super(maxUnaccessedTime);
    }
}
