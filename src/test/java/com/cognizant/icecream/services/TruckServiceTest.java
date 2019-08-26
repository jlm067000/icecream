package com.cognizant.icecream.services;

import com.cognizant.icecream.clients.GarageCRUD;
import com.cognizant.icecream.mock.MockFactory;
import com.cognizant.icecream.pools.api.ResultPool;
import com.cognizant.icecream.pools.api.ServiceResultPool;
import com.cognizant.icecream.result.Result;
import com.cognizant.icecream.clients.TruckCRUD;
import com.cognizant.icecream.clients.TruckPurchasingClient;
import com.cognizant.icecream.models.Truck;
import com.cognizant.icecream.result.ServiceResult;
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

    private static final String PERSISTED_VIN = "12";
    private static final String UNPERSISTED_VIN = "11";

    private static Truck alcoholic;
    private static Truck nonalcoholic;
    private static Truck unpersisted;
    private static Truck invalid;

    private static Object dontcare;

    private GarageCRUD garageCRUD;
    private TruckCRUD truckCRUD;
    private TruckPurchasingClient purchasingClient;
    private ResultPool resultPool;
    private ServiceResultPool<Truck> serviceResultPool;

    private TruckService truckService;

    @BeforeClass
    public static void init() {

        alcoholic = generateTruck(PERSISTED_VIN, true);
        nonalcoholic = generateTruck(PERSISTED_VIN, false);
        unpersisted = generateTruck(UNPERSISTED_VIN, true);
        invalid = new Truck();
        dontcare = new Object();
    }

    @Before
    public void setup() {

        garageCRUD = Mockito.mock(GarageCRUD.class);
        truckCRUD = MockFactory.createTruckCRUD(alcoholic, nonalcoholic, unpersisted);
        purchasingClient = Mockito.mock(TruckPurchasingClient.class);
        resultPool = MockFactory.createResultPool();
        serviceResultPool = MockFactory.createServiceResultPool();

        when(truckCRUD.findByVIN(PERSISTED_VIN)).thenReturn(Optional.of(alcoholic));

        Answer<Optional<Truck>> truckAnswer = iom -> Optional.of(iom.getArgument(0));

        when(truckCRUD.add(any())).then(truckAnswer);
        when(truckCRUD.update(nonalcoholic)).thenReturn(Optional.of(nonalcoholic));
        when(truckCRUD.update(unpersisted)).thenReturn(Optional.empty());
        when(truckCRUD.remove(PERSISTED_VIN)).thenReturn(true);
        when(truckCRUD.remove(UNPERSISTED_VIN)).thenReturn(false);

        truckService = new TruckService(garageCRUD, truckCRUD, purchasingClient, resultPool, serviceResultPool);
    }

    @Test
    public void testGetTruck() {

        truckService.getTruck(PERSISTED_VIN, this::testGetPersisted);
        truckService.getTruck(UNPERSISTED_VIN, this::testGetUnpersisted);
    }

    private Object testGetPersisted(ServiceResult<Truck> result) {

        assertNotNull(result);
        assertTrue(result.isSuccess());

        Truck truck = result.getPayload();

        assertEquals(truck.getVin(), PERSISTED_VIN);
        verify(truckCRUD).findByVIN(PERSISTED_VIN);

        return dontcare;
    }

    private Object testGetUnpersisted(ServiceResult<Truck> result) {

        assertNotNull(result);
        assertFalse(result.isSuccess());

        Truck truck = result.getPayload();

        assertNull(truck);
        verify(truckCRUD).findByVIN(UNPERSISTED_VIN);

        return dontcare;
    }

    @Test
    public void testAddTruck() {

        truckService.addTruck(unpersisted, this::testAddUnpersisted);
        truckService.addTruck(invalid, this::testAddInvalid);
    }

    private Object testAddUnpersisted(ServiceResult<Truck> result) {

        assertNotNull(result);
        assertTrue(result.isSuccess());

        Truck added = result.getPayload();

        assertEquals(added, unpersisted);
        verify(truckCRUD).add(unpersisted);

        return dontcare;
    }

    private Object testAddInvalid(ServiceResult<Truck> result) {

        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertNull(result.getPayload());
        verify(truckCRUD).add(invalid);

        return dontcare;
    }

    @Test
    public void testUpdateTruck() {

        truckService.updateTruck(nonalcoholic, this::testUpdateNonalchoholic);
        truckService.updateTruck(unpersisted, this::testUpdateUnpersisted);
    }

    private Object testUpdateNonalchoholic(ServiceResult<Truck> result) {

        assertNotNull(result);
        assertTrue(result.isSuccess());

        Truck updated = result.getPayload();

        assertEquals(updated, nonalcoholic);
        verify(truckCRUD).update(nonalcoholic);

        return dontcare;
    }

    private Object testUpdateUnpersisted(ServiceResult<Truck> result) {

        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertNull(result.getPayload());
        verify(truckCRUD).update(unpersisted);

        return dontcare;
    }

    @Test
    public void testRemoveTruck() {

        Result result = truckService.removeTruck(PERSISTED_VIN);

        assertTrue(result.isSuccess());
        verify(truckCRUD).remove(PERSISTED_VIN);

        result = truckService.removeTruck(UNPERSISTED_VIN);

        assertFalse(result.isSuccess());
        verify(truckCRUD).remove(UNPERSISTED_VIN);
    }

    private static Truck generateTruck(String VIN, boolean alcoholic) {

        Truck truck = new Truck();
        truck.setVin(VIN);
        truck.setAlcoholic(alcoholic);

        return truck;
    }
}
