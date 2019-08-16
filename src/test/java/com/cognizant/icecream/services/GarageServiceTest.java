package com.cognizant.icecream.services;

import com.cognizant.icecream.clients.GarageCRUD;
import com.cognizant.icecream.clients.Result;
import com.cognizant.icecream.clients.SupplyClient;
import com.cognizant.icecream.clients.TimeClient;
import com.cognizant.icecream.models.Garage;
import com.cognizant.icecream.models.TimeSlot;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GarageServiceTest {

    private static final String VALID_CODE = "12";
    private static final String INVALID_CODE = "11";

    private static TimeSlot futureTime;
    private static TimeSlot pastTime;
    private static TimeSlot invalidHour;
    private static TimeSlot nullDate;

    private GarageService garageService;
    private GarageCRUD garageCRUD;
    private SupplyClient supplyClient;
    private TimeClient timeClient;

    @BeforeClass
    public static void init() {
        futureTime = getTimeSlot(1);
        pastTime = getTimeSlot(-1);
    }

    @Before
    public void setup() {

        Garage garage = new Garage();
        garage.setCode(VALID_CODE);

        garageCRUD = Mockito.mock(GarageCRUD.class);
        when(garageCRUD.findByCode(VALID_CODE)).thenReturn(Optional.of(garage));
        when(garageCRUD.findByCode(INVALID_CODE)).thenReturn(Optional.empty());
        when(garageCRUD.remove(VALID_CODE)).thenReturn(true);
        when(garageCRUD.remove(INVALID_CODE)).thenReturn(false);

        Answer<Optional<Garage>> crudAnswer = iom -> Optional.of(iom.getArgument(0));
        when(garageCRUD.add(any())).then(crudAnswer);
        when(garageCRUD.update(any())).then(crudAnswer);

        supplyClient = Mockito.mock(SupplyClient.class);
        when(supplyClient.scheduleResupply(any(), any())).thenReturn(true);

        timeClient = Mockito.mock(TimeClient.class);

        when(timeClient.isValid(futureTime)).thenReturn(true);
        when(timeClient.isValid(pastTime)).thenReturn(false);

        garageService = new GarageService(garageCRUD, supplyClient, timeClient);
    }

    @Test
    public void testResupply() {

        Result result = garageService.resupply(VALID_CODE, futureTime);
        assertTrue(result.isSuccess());
        verify(supplyClient).scheduleResupply(VALID_CODE, futureTime);

        result = garageService.resupply(INVALID_CODE, futureTime);
        assertFalse(result.isSuccess());

        result = garageService.resupply(VALID_CODE, pastTime);
        assertFalse(result.isSuccess());
    }

    @Test
    public void testGetGarage() {

        Garage garage = garageService.getGarage(VALID_CODE);

        assertNotNull(garage);
        assertEquals(VALID_CODE, garage.getCode());
        verify(garageCRUD).findByCode(VALID_CODE);

        garage = garageService.getGarage(INVALID_CODE);

        assertNull(garage);
        verify(garageCRUD).findByCode(INVALID_CODE);
    }

    @Test
    public void testAddGarage() {

        Garage newGarage = new Garage();
        Garage added = garageService.addGarage(newGarage);

        assertEquals(added, newGarage);
        verify(garageCRUD).add(newGarage);
    }

    @Test
    public void testUpdateGarage() {

        Garage current = new Garage();
        current.setCode(VALID_CODE);

        Garage updated = garageService.updateGarage(current);

        assertEquals(current, updated);
        verify(garageCRUD).update(current);
    }

    @Test
    public void testRemoveGarage() {

        Result result = garageService.removeGarage(VALID_CODE);

        assertTrue(result.isSuccess());
        verify(garageCRUD).remove(VALID_CODE);

        result = garageService.removeGarage(INVALID_CODE);

        assertFalse(result.isSuccess());
        verify(garageCRUD).remove(INVALID_CODE);
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
}
