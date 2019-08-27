package com.cognizant.icecream.pools.eviction;

import com.cognizant.icecream.models.Invoice;
import com.cognizant.icecream.result.MutableServiceResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InvoiceResultEvictionPolicy extends UnaccessedTimeEvictionPolicy<MutableServiceResult<Invoice>> {

    InvoiceResultEvictionPolicy(@Value("${objectPools.serviceResult.maxUnaccessedTime}") long maxUnaccessedTime) {
        super(maxUnaccessedTime);
    }
}
