package com.cognizant.icecream.clients;

import com.cognizant.icecream.models.TimeSlot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

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

        LocalDate current = LocalDate.now();

        return false;
    }

}
