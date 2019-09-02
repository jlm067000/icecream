package com.cognizant.icecream.components.api;

import com.cognizant.icecream.models.Invoice;
import com.cognizant.icecream.models.TruckPurchaseOrder;
import com.cognizant.icecream.result.ServiceResultProcessor;

public interface TruckPurchaser {

    <T> T purchaseTrucks(String authorization, TruckPurchaseOrder order, ServiceResultProcessor<Invoice, T> resultProcessor);
}
