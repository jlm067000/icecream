package com.cognizant.icecream.mock;

import com.cognizant.icecream.clients.GarageCRUD;
import com.cognizant.icecream.clients.TruckCRUD;
import com.cognizant.icecream.models.Garage;
import com.cognizant.icecream.models.Truck;
import com.cognizant.icecream.pools.api.ResultPool;
import com.cognizant.icecream.pools.api.ServiceResultPool;

public class MockFactory {

    public static GarageCRUD createGarageCRUD(Garage persisted, Garage newGarage) {

        return GarageCRUDFactory.createMock(persisted, newGarage);
    }

    public static TruckCRUD createTruckCRUD(Truck alcoholic, Truck nonalcoholic, Truck unpersisted) {

        return TruckCRUDFactory.createMock(alcoholic, nonalcoholic, unpersisted);
    }

    public static <T> ServiceResultPool<T> createServiceResultPool() {

        return ServiceResultPoolFactory.createServiceResultPool();
    }

    public static ResultPool createResultPool() {
        return ResultPoolFactory.createResultPool();
    }

    public static <T> DefaultAbandonedConfigMock<T> createAbandonedConfig(int timeout) {

        return new DefaultAbandonedConfigMock<>(timeout);
    }
}
