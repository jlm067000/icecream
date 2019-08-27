package com.cognizant.icecream.pools.config.abandoned;

import com.cognizant.icecream.models.Invoice;
import com.cognizant.icecream.result.MutableServiceResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InvoiceResultAbandonedConfig extends DefaultAbandonedConfig<MutableServiceResult<Invoice>> {

    InvoiceResultAbandonedConfig(@Value("${objectPools.serviceResult.maxUnaccessedTime}") int maxUnaccessedTime) {
        super(maxUnaccessedTime);
    }
}
