package com.cognizant.icecream.components;

import com.cognizant.icecream.clients.TruckPurchasingClient;
import com.cognizant.icecream.components.api.GarageCache;
import com.cognizant.icecream.components.api.TruckCRUDOperator;
import com.cognizant.icecream.components.api.TruckPurchaser;
import com.cognizant.icecream.models.Invoice;
import com.cognizant.icecream.models.Truck;
import com.cognizant.icecream.models.TruckPurchaseOrder;
import com.cognizant.icecream.pools.api.ServiceResultPool;
import com.cognizant.icecream.result.MutableServiceResult;
import com.cognizant.icecream.result.ServiceResultProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.Set;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
class TruckPurchaserBean implements TruckPurchaser {

    private static final String COULD_NOT_PURCHASE = "Failed to process Purchase Order. " +
                                    "Contact Accounting Department for more information.";
    private static final String PREEXISTING_TRUCK = "Could not process Purchase Order because truck of VIN %s " +
                                    "has already been purchased.";
    private static final String INVALID_GARAGE = "Garage with code %s does not exist.";

    private TruckPurchasingClient purchasingClient;
    private ServiceResultPool<Invoice> invoiceResultPool;
    private GarageCache garageCache;
    private TruckCRUDOperator crudOperator;

    private String authorization;

    @Autowired
    TruckPurchaserBean(
            TruckPurchasingClient purchasingClient,
            ServiceResultPool<Invoice> invoiceResultPool,
            GarageCache garageCache,
            TruckCRUDOperator crudOperator,
            HttpServletRequest request
    ) {
        this.purchasingClient = purchasingClient;
        this.invoiceResultPool = invoiceResultPool;
        this.garageCache = garageCache;
        this.crudOperator = crudOperator;

        this.authorization = request.getHeader("Authorization");
    }

    @Override
    public <T> T purchaseTrucks(TruckPurchaseOrder order, ServiceResultProcessor<Invoice, T> resultProcessor) {

        Truck preexisting = findPreexisting(authorization, order.getTrucks());

        if(preexisting != null) {
            return processPurchaseError(PREEXISTING_TRUCK, preexisting.getVin(), resultProcessor);
        }

        if(!garageCache.validate(authorization, order.getGarage())) {
            return processPurchaseError(INVALID_GARAGE, order.getGarage().getCode(), resultProcessor);
        }

        Optional<Invoice> invoice = purchasingClient.purchaseTrucks(authorization, order);

        return ComponentsUtil.processOptional(invoice, COULD_NOT_PURCHASE, invoiceResultPool, resultProcessor);
    }

    private <T> T processPurchaseError(String formatErrStr, Object formatArg, ServiceResultProcessor<Invoice, T> resultProcessor) {

        String errMsg = String.format(formatErrStr, formatArg);
        MutableServiceResult<Invoice> result = ComponentsUtil.createResult(false, errMsg, null, invoiceResultPool);

        return ComponentsUtil.processResult(result, resultProcessor, invoiceResultPool);
    }

    private Truck findPreexisting(String authorization, Set<Truck> trucks) {

        for(Truck truck : trucks) {

            if(crudOperator.exists(authorization, truck)) {
                return truck;
            }
        }

        return null;
    }
}
