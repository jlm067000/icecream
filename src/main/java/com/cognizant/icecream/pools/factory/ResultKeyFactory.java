package com.cognizant.icecream.pools.factory;

import com.cognizant.icecream.models.result.MutableServiceResult;
import org.springframework.stereotype.Component;

@Component
class ResultKeyFactory extends KeyFactory<MutableServiceResult> {

    ResultKeyFactory() {
        super(MutableServiceResult.class);
    }
}
