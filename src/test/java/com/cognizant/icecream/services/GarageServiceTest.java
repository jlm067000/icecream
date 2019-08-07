package com.cognizant.icecream.services;

import com.cognizant.icecream.clients.GarageCRUD;
import com.cognizant.icecream.clients.Result;
import com.cognizant.icecream.models.Garage;
import com.cognizant.icecream.models.TimeSlot;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GarageServiceTest {

    private static TimeSlot futureTime;
    private static TimeSlot pastTime;
    private static TimeSlot invalidHour;
    private static TimeSlot nullDate;

    private GarageService garageService;
    private GarageCRUD garageCRUD;

    @BeforeClass
    public static void init() {
        futureTime = getTimeSlot(1);
        pastTime = getTimeSlot(-1);
        invalidHour = getTimeSlot(futureTime.getDate(), 24);
        nullDate = getTimeSlot(null, futureTime.getHour());
    }

    @Before
    public void setup() {

        Garage garage = new Garage();
        garage.setCode("12");

        garageCRUD = Mockito.mock(GarageCRUD.class);
        Mockito.when(garageCRUD.findByCode("12")).thenReturn(Optional.of(garage));

        garageService = new GarageService(garageCRUD);
    }

    @Test
    public void testResupply() {

        Result result = garageService.resupply("12", futureTime);
        assertTrue(result.isSuccess());

        result = garageService.resupply("11", futureTime);
        assertFalse(result.isSuccess());

        result = garageService.resupply("12", pastTime);
        assertFalse(result.isSuccess());

        result = garageService.resupply("12", nullDate);
        assertFalse(result.isSuccess());

        result = garageService.resupply("12", invalidHour);
        assertFalse(result.isSuccess());
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
