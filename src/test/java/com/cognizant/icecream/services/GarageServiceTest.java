package com.cognizant.icecream.services;

import com.cognizant.icecream.clients.GarageCRUD;
import com.cognizant.icecream.clients.SupplyClient;
import com.cognizant.icecream.clients.TimeClient;
import com.cognizant.icecream.mock.MockFactory;
import com.cognizant.icecream.models.Garage;
import com.cognizant.icecream.models.TimeSlot;
import com.cognizant.icecream.pools.api.ServiceResultPool;
import com.cognizant.icecream.result.ServiceResult;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    private GarageService garageService;
    private GarageCRUD garageCRUD;
    private SupplyClient supplyClient;
    private TimeClient timeClient;
    private ServiceResultPool resultPool;

    @BeforeClass
    public static void init() {

        futureTime = getTimeSlot(1);
        pastTime = getTimeSlot(-1);

        persisted = generateGarage(PERSISTED_CODE);
        newGarage = generateGarage(UNPERSISTED_CODE);

        invalidGarage = new Garage();
    }

    @Before
    public void setup() {

        garageCRUD = MockFactory.createGarageCRUD(persisted, newGarage);

        supplyClient = Mockito.mock(SupplyClient.class);
        when(supplyClient.scheduleResupply(any(), any())).thenReturn(true);

        timeClient = Mockito.mock(TimeClient.class);

        when(timeClient.isValid(futureTime)).thenReturn(true);
        when(timeClient.isValid(pastTime)).thenReturn(false);

        garageService = new GarageService(garageCRUD, supplyClient, timeClient);
    }

    @Test
    public void testResupply() {

        ServiceResult<Void> result = garageService.resupply(PERSISTED_CODE, futureTime);
        assertTrue(result.isSuccess());
        verify(supplyClient).scheduleResupply(PERSISTED_CODE, futureTime);

        result = garageService.resupply(UNPERSISTED_CODE, futureTime);
        assertFalse(result.isSuccess());

        result = garageService.resupply(PERSISTED_CODE, pastTime);
        assertFalse(result.isSuccess());
    }

    @Test
    public void testGetGarage() {

        ServiceResult<Garage> result = garageService.getGarage(PERSISTED_CODE);

        assertTrue(result.isSuccess());
        assertNotNull(result.getPayload());
        assertEquals(PERSISTED_CODE, result.getPayload().getCode());
        verify(garageCRUD).findByCode(PERSISTED_CODE);

        result = garageService.getGarage(UNPERSISTED_CODE);

        assertFalse(result.isSuccess());
        assertNull(result.getPayload());
        verify(garageCRUD).findByCode(UNPERSISTED_CODE);
    }

    @Test
    public void testAddGarage() {

        ServiceResult<Garage> result = garageService.addGarage(newGarage);

        assertTrue(result.isSuccess());
        assertEquals(result.getPayload(), newGarage);
        verify(garageCRUD).add(newGarage);

        result = garageService.addGarage(invalidGarage);

        assertFalse(result.isSuccess());
        assertNull(result.getPayload());
        verify(garageCRUD).add(invalidGarage);
    }

    @Test
    public void testUpdateGarage() {

        Garage current = new Garage();
        current.setCode(PERSISTED_CODE);

        ServiceResult<Garage> result = garageService.updateGarage(current);

        assertTrue(result.isSuccess());
        assertEquals(current, result.getPayload());
        verify(garageCRUD).update(current);

        result = garageService.updateGarage(newGarage);

        assertFalse(result.isSuccess());
        assertNull(result.getPayload());
        verify(garageCRUD).update(newGarage);
    }

    @Test
    public void testRemoveGarage() {

        ServiceResult<Void> result = garageService.removeGarage(PERSISTED_CODE);

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
