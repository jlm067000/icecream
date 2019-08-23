package com.cognizant.icecream.pools.eviction;

import com.cognizant.icecream.result.MutableResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ResultEvictionPolicy extends UnaccessedTimeEvictionPolicy<MutableResult> {


    ResultEvictionPolicy(@Value("${objectPools.result.maxUnaccessedTime}") long maxUnaccessedTime) {
        super(maxUnaccessedTime);
    }
}
