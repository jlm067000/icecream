package com.cognizant.icecream.validation;

import com.cognizant.icecream.models.TimeSlot;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class FutureTimeValidator implements ConstraintValidator<FutureTime, TimeSlot> {

    @Override
    public boolean isValid(TimeSlot timeSlot, ConstraintValidatorContext context) {

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
