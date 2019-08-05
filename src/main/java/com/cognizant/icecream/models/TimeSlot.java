package com.cognizant.icecream.models;

import com.cognizant.icecream.validation.FutureTime;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@FutureTime(message = "Not a future time slot. Must indicate future time.")
public class TimeSlot {

    @NotNull
    private LocalDate date;

    @Max(23)
    @Min(0)
    private int hour;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }
}
