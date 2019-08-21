package com.cognizant.icecream.pools.factory;

import com.cognizant.icecream.result.ServiceResult;
import org.springframework.stereotype.Component;

@Component
class ServiceResultKeyFactory extends KeyFactory<ServiceResult> {

    ServiceResultKeyFactory() {
        super(ServiceResult.class);
    }
}
