package com.cognizant.icecream.pools.factory;

import com.cognizant.icecream.result.ServiceResult;
import org.springframework.stereotype.Component;

@Component
class ResultKeyFactory extends KeyFactory<ServiceResult> {

    ResultKeyFactory() {
        super(ServiceResult.class);
    }
}
