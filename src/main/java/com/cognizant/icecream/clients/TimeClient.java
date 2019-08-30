package com.cognizant.icecream.clients;

import com.cognizant.icecream.models.TimeSlot;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class TimeClient {

    public boolean isValid(String authorization, TimeSlot timeSlot) {
        return futureTime(timeSlot.getDate(), timeSlot.getHour());
    }

    public boolean isValid(TimeSlot timeSlot) {
        return futureTime(timeSlot.getDate(), timeSlot.getHour());
    }

    private static boolean futureTime(LocalDate date, int hour) {

        LocalDateTime current = LocalDateTime.now();
        LocalDate today = current.toLocalDate();
        int currentHour = current.getHour();

        if(date.isAfter(today)) {
            return true;
        }

        if(date.isBefore(today)) {
            return false;
        }

        if(hour > currentHour) {
            return true;
        }

        return false;
    }
}
