package com.cognizant.icecream.clients;

import com.cognizant.icecream.models.TimeSlot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class SupplyClient {

    private GarageCRUD garageCRUD;

    @Autowired
    public SupplyClient(GarageCRUD garageCRUD) {
        this.garageCRUD = garageCRUD;
    }

    public boolean scheduleResupply(String code, TimeSlot timeSlot) {

        if(garageCRUD.findByCode(code).isEmpty()) {
            return false;
        }

        if(!validInstance(timeSlot)) {
            return false;
        }

        if(!validTime(timeSlot)) {
            return false;
        }

        try {
            Thread.sleep(1000);
        }
        catch(InterruptedException ex) {}

        return true;
    }

    private static boolean validInstance(TimeSlot timeSlot) {

        if(timeSlot == null) {
            return false;
        }

        if(timeSlot.getDate() == null) {
            return false;
        }

        if(timeSlot.getHour() < 0) {
            return false;
        }

        if(timeSlot.getHour() > 23) {
            return false;
        }

        return true;
    }

    private static boolean validTime(TimeSlot timeSlot) {

        LocalDate date = timeSlot.getDate();
        int hour = timeSlot.getHour();

        LocalDateTime present = LocalDateTime.now();

        Optional<Boolean> valid = validTime(date, present.toLocalDate());

        if(valid.isPresent()) {
            return valid.get();
        }

        return validTime(hour, present.getHour());
    }

    private static Optional<Boolean> validTime(LocalDate date, LocalDate today) {

        if(date.isBefore(today)) {
            return Optional.of(Boolean.FALSE);
        }

        if(date.isAfter(today)) {
            return Optional.of(Boolean.TRUE);
        }

        return Optional.empty();
    }

    private static boolean validTime(int hour, int currentHour) {

        if(hour > currentHour) {
            return true;
        }
        else {
            return false;
        }
    }

}
