package com.cognizant.icecream.services;

import com.cognizant.icecream.clients.GarageCRUD;
import com.cognizant.icecream.clients.SupplyClient;
import com.cognizant.icecream.clients.TimeClient;
import com.cognizant.icecream.mock.MockFactory;
import com.cognizant.icecream.models.Garage;
import com.cognizant.icecream.models.TimeSlot;
import com.cognizant.icecream.pools.api.ResultPool;
import com.cognizant.icecream.pools.api.ServiceResultPool;
import com.cognizant.icecream.result.Result;
import com.cognizant.icecream.result.ServiceResult;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Function;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GarageServiceTest {

    private static final String PERSISTED_CODE = "12";
    private static final String UNPERSISTED_CODE = "11";

    private static TimeSlot futureTime;
    private static TimeSlot pastTime;

    private static Garage persisted;
    private static Garage newGarage;
    private static Garage invalidGarage;

    private static Object dontcare;

    private GarageService garageService;
    private GarageCRUD garageCRUD;
    private SupplyClient supplyClient;
    private TimeClient timeClient;
    private ServiceResultPool<Garage> serviceResultPool;
    private ResultPool resultPool;

    @BeforeClass
    public static void init() {

        futureTime = getTimeSlot(1);
        pastTime = getTimeSlot(-1);

        persisted = generateGarage(PERSISTED_CODE);
        newGarage = generateGarage(UNPERSISTED_CODE);

        invalidGarage = new Garage();

        dontcare = new Object();
    }

    @Before
    public void setup() {

        garageCRUD = MockFactory.createGarageCRUD(persisted, newGarage);
        serviceResultPool = MockFactory.createServiceResultPool();
        resultPool = MockFactory.createResultPool();

        supplyClient = Mockito.mock(SupplyClient.class);
        when(supplyClient.scheduleResupply(any(), any())).thenReturn(true);

        timeClient = Mockito.mock(TimeClient.class);

        when(timeClient.isValid(futureTime)).thenReturn(true);
        when(timeClient.isValid(pastTime)).thenReturn(false);

        garageService = new GarageService(garageCRUD, supplyClient, timeClient, serviceResultPool, resultPool);
    }

    @Test
    public void testResupply() {

        Result result = garageService.resupply(PERSISTED_CODE, futureTime);
        assertTrue(result.isSuccess());
        verify(supplyClient).scheduleResupply(PERSISTED_CODE, futureTime);

        result = garageService.resupply(UNPERSISTED_CODE, futureTime);
        assertFalse(result.isSuccess());

        result = garageService.resupply(PERSISTED_CODE, pastTime);
        assertFalse(result.isSuccess());
    }

    @Test
    public void testGetGarage() {

        garageService.getGarage(PERSISTED_CODE, this::testGetValidGarage);
        verify(garageCRUD).findByCode(PERSISTED_CODE);

        garageService.getGarage(UNPERSISTED_CODE, this::testGetInvalidGarage);
        verify(garageCRUD).findByCode(UNPERSISTED_CODE);
    }

    private Object testGetValidGarage(ServiceResult<?> result) {

        assertTrue(result.isSuccess());
        assertNotNull(result.getPayload());
        assertEquals(PERSISTED_CODE, ((Garage)result.getPayload()).getCode());

        return dontcare;
    }

    private Object testGetInvalidGarage(ServiceResult<?> result) {

        assertFalse(result.isSuccess());
        assertNull(result.getPayload());

        return dontcare;
    }

    @Test
    public void testAddGarage() {

        garageService.addGarage(newGarage, this::testAddNewGarage);
        verify(garageCRUD).add(newGarage);

        garageService.addGarage(invalidGarage, this::testAddInvalidGarage);
        verify(garageCRUD).add(invalidGarage);
    }

    private Object testAddNewGarage(ServiceResult<?> result) {

        assertTrue(result.isSuccess());
        assertEquals(result.getPayload(), newGarage);

        return dontcare;
    }

    private Object testAddInvalidGarage(ServiceResult<?> result) {

        assertFalse(result.isSuccess());
        assertNull(result.getPayload());

        return dontcare;
    }

    @Test
    public void testUpdateGarage() {

        Garage current = new Garage();
        current.setCode(PERSISTED_CODE);
        Function<ServiceResult, Object> resultProcessor = r -> testUpdateCurrentGarage(r, current);

        garageService.updateGarage(current, resultProcessor);
        verify(garageCRUD).update(current);

        garageService.updateGarage(newGarage, this::testUpdateNewGarage);
        verify(garageCRUD).update(newGarage);
    }

    private Object testUpdateCurrentGarage(ServiceResult<?> result, Garage current) {

        assertTrue(result.isSuccess());
        assertEquals(current, result.getPayload());

        return dontcare;
    }

    private Object testUpdateNewGarage(ServiceResult<?> result) {

        assertFalse(result.isSuccess());
        assertNull(result.getPayload());

        return dontcare;
    }

    @Test
    public void testRemoveGarage() {

        Result result = garageService.removeGarage(PERSISTED_CODE);

        assertTrue(result.isSuccess());
        verify(garageCRUD).remove(PERSISTED_CODE);

        result = garageService.removeGarage(UNPERSISTED_CODE);

        assertFalse(result.isSuccess());
        verify(garageCRUD).remove(UNPERSISTED_CODE);
    }

    private static TimeSlot getTimeSlot(int dayOffset) {

        LocalDateTime present = LocalDateTime.now();
        LocalDate date = present.toLocalDate().plusDays(dayOffset);

        return getTimeSlot(date, present.getHour());
    }

    private static TimeSlot getTimeSlot(LocalDate date, int hour) {

        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setDate(date);
        timeSlot.setHour(hour);

        return timeSlot;
    }

    private static Garage generateGarage(String code) {

        Garage garage = new Garage();
        garage.setCode(code);

        return garage;
    }
}
