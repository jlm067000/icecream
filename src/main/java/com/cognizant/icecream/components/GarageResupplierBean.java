package com.cognizant.icecream.components;

import com.cognizant.icecream.clients.GarageCRUD;
import com.cognizant.icecream.clients.SupplyClient;
import com.cognizant.icecream.clients.TimeClient;
import com.cognizant.icecream.components.api.GarageResupplier;
import com.cognizant.icecream.models.TimeSlot;
import com.cognizant.icecream.pools.api.ResultPool;
import com.cognizant.icecream.result.MutableResult;
import com.cognizant.icecream.result.Result;
import com.cognizant.icecream.result.ResultFactory;
import com.cognizant.icecream.result.ResultProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
class GarageResupplierBean implements GarageResupplier {

    private static final Result FUTURE;
    private static final Result SCHEDULED;
    private static final Result COULD_NOT_RESUPPLY;
    private static final String NOT_FOUND = "Garage %s not found.";

    private GarageCRUD garageCRUD;
    private SupplyClient supplyClient;
    private TimeClient timeClient;
    private ResultPool resultPool;

    private String authorization;

    static {
        FUTURE = ResultFactory.createResult(false, "Resupply must be SCHEDULED for a FUTURE time slot.");
        SCHEDULED = ResultFactory.createResult(true, "SCHEDULED");
        COULD_NOT_RESUPPLY = ResultFactory.createResult(false, "Supply Service was not able to schedule resupply");
    }

    @Autowired
    GarageResupplierBean(
            GarageCRUD garageCRUD,
            SupplyClient supplyClient,
            TimeClient timeClient,
            ResultPool resultPool,
            HttpServletRequest request
    ) {
        this.garageCRUD = garageCRUD;
        this.supplyClient = supplyClient;
        this.timeClient = timeClient;
        this.resultPool = resultPool;
        this.authorization = request.getHeader("Authorization");
    }

    @Override
    public <T> T resupply(String garageCode, TimeSlot timeSlot, ResultProcessor<T> resultProcessor) {

        if(!timeClient.isValid(authorization, timeSlot)) {
            return resultProcessor.apply(FUTURE);
        }

        if(!garageCRUD.findByCode(authorization, garageCode).isPresent()) {

            String errMsg = String.format(NOT_FOUND, garageCode);
            MutableResult result = ComponentsUtil.createResult(false, errMsg, resultPool);

            return ComponentsUtil.processResult(result, resultProcessor, resultPool);
        }

        boolean success = supplyClient.scheduleResupply(authorization, garageCode, timeSlot);

        return success ? resultProcessor.apply(SCHEDULED) : resultProcessor.apply(COULD_NOT_RESUPPLY);
    }
}
