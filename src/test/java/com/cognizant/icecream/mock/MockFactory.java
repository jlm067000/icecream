package com.cognizant.icecream.mock;

import com.cognizant.icecream.clients.GarageCRUD;
import com.cognizant.icecream.models.Garage;

public class MockFactory {

    public static GarageCRUD createGarageCRUD(Garage persisted, Garage newGarage) {

        return GarageCRUDFactory.createMock(persisted, newGarage);
    }
}
