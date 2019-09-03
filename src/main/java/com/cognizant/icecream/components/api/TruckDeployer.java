package com.cognizant.icecream.components.api;

import com.cognizant.icecream.models.Neighborhood;
import com.cognizant.icecream.models.TruckGarage;
import com.cognizant.icecream.result.ResultProcessor;

public interface TruckDeployer {

    <T> T deploy(TruckGarage truckGarage, ResultProcessor<T> resultProcessor);
    <T> T patrol(boolean alcoholic, Neighborhood neighborhood, ResultProcessor<T> resultProcessor);
}
