package com.cognizant.icecream.pools.garage;

import com.cognizant.icecream.models.Garage;
import com.cognizant.icecream.pools.ServiceResultKey;

public class GarageResultKey implements ServiceResultKey<Garage> {



    @Override
    public Integer getIndex() {
        return null;
    }

    @Override
    public Class<Garage> getPayloadType() {
        return null;
    }
}
