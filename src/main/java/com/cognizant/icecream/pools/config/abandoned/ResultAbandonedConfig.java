package com.cognizant.icecream.pools.config.abandoned;

import com.cognizant.icecream.result.MutableResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ResultAbandonedConfig extends DefaultAbandonedConfig<MutableResult> {

    ResultAbandonedConfig(@Value("${objectPools.result.maxUnaccessedTime}") int maxUnaccessedTime) {
        super(maxUnaccessedTime);
    }
}
