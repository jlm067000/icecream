package com.cognizant.icecream.components.api;

import com.cognizant.icecream.models.TimeSlot;
import com.cognizant.icecream.result.ResultProcessor;

public interface GarageResupplier {

    <T> T resupply(String garageCode, TimeSlot timeSlot, ResultProcessor<T> resultProcessor);
}
