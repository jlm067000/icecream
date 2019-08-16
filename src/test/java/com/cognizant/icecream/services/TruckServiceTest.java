package com.cognizant.icecream.services;

import com.cognizant.icecream.clients.GarageCRUD;
import com.cognizant.icecream.clients.TruckCRUD;
import com.cognizant.icecream.clients.TruckPurchasingClient;
import com.cognizant.icecream.models.Truck;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TruckServiceTest {

    private static final String VIN12 = "12";
    private static final String INVALID_VIN = "11";

    private static Truck alcoholic12;

    private GarageCRUD garageCRUD;
    private TruckCRUD truckCRUD;
    private TruckPurchasingClient purchasingClient;

    private TruckService truckService;

    @BeforeClass
    public static void init() {

        alcoholic12 = generateTruck("12", true);
    }

    @Before
    public void setup() {

        garageCRUD = Mockito.mock(GarageCRUD.class);
        truckCRUD = Mockito.mock(TruckCRUD.class);
        purchasingClient = Mockito.mock(TruckPurchasingClient.class);

        when(truckCRUD.findByVIN(VIN12)).thenReturn(Optional.of(alcoholic12));

        truckService = new TruckService(garageCRUD, truckCRUD, purchasingClient);
    }

    @Test
    public void testGetTruck() {

        Truck truck = truckService.getTruck(VIN12);

        assertNotNull(truck);
        assertEquals(truck.getVin(), VIN12);
        verify(truckCRUD).findByVIN(VIN12);

        truck = truckService.getTruck(INVALID_VIN);

        assertNull(truck);
        verify(truckCRUD).findByVIN(INVALID_VIN);
    }


    private static Truck generateTruck(String VIN, boolean alcoholic) {

        Truck truck = new Truck();
        truck.setVin(VIN);
        truck.setAlcoholic(alcoholic);

        return truck;
    }
}
