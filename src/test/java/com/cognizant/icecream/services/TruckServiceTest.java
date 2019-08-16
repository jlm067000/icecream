package com.cognizant.icecream.services;

import com.cognizant.icecream.clients.GarageCRUD;
import com.cognizant.icecream.clients.Result;
import com.cognizant.icecream.clients.TruckCRUD;
import com.cognizant.icecream.clients.TruckPurchasingClient;
import com.cognizant.icecream.models.Truck;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TruckServiceTest {

    private static final String VIN12 = "12";
    private static final String INVALID_VIN = "11";

    private static Truck alcoholic12;
    private static Truck nonalcoholic12;
    private static Truck unpersisted;

    private GarageCRUD garageCRUD;
    private TruckCRUD truckCRUD;
    private TruckPurchasingClient purchasingClient;

    private TruckService truckService;

    @BeforeClass
    public static void init() {

        alcoholic12 = generateTruck(VIN12, true);
        nonalcoholic12 = generateTruck(VIN12, false);
        unpersisted = generateTruck(INVALID_VIN, true);
    }

    @Before
    public void setup() {

        garageCRUD = Mockito.mock(GarageCRUD.class);
        truckCRUD = Mockito.mock(TruckCRUD.class);
        purchasingClient = Mockito.mock(TruckPurchasingClient.class);

        when(truckCRUD.findByVIN(VIN12)).thenReturn(Optional.of(alcoholic12));

        Answer<Optional<Truck>> truckAnswer = iom -> Optional.of(iom.getArgument(0));

        when(truckCRUD.add(any())).then(truckAnswer);
        when(truckCRUD.update(nonalcoholic12)).thenReturn(Optional.of(nonalcoholic12));
        when(truckCRUD.update(unpersisted)).thenReturn(Optional.empty());
        when(truckCRUD.remove(VIN12)).thenReturn(true);
        when(truckCRUD.remove(INVALID_VIN)).thenReturn(false);

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

    @Test
    public void testAddTruck() {

        Truck newTruck = new Truck();
        Truck added = truckService.addTruck(newTruck);

        assertEquals(added, newTruck);
        verify(truckCRUD).add(newTruck);
    }

    @Test
    public void testUpdateTruck() {

        Truck updated = truckService.updateTruck(nonalcoholic12);

        assertEquals(updated, nonalcoholic12);
        verify(truckCRUD).update(nonalcoholic12);

        updated = truckService.updateTruck(unpersisted);

        assertNull(updated);
        verify(truckCRUD).update(unpersisted);
    }

    @Test
    public void testRemoveTruck() {

        Result result = truckService.removeTruck(VIN12);

        assertTrue(result.isSuccess());
        verify(truckCRUD).remove(VIN12);

        result = truckService.removeTruck(INVALID_VIN);

        assertFalse(result.isSuccess());
        verify(truckCRUD).remove(INVALID_VIN);
    }

    private static Truck generateTruck(String VIN, boolean alcoholic) {

        Truck truck = new Truck();
        truck.setVin(VIN);
        truck.setAlcoholic(alcoholic);

        return truck;
    }
}
