package com.nikesh.car.rental.model;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Reservation {

    private final Car car;
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;

    public Reservation(final Car car, final LocalDateTime startDateTime, final int numberOfDays) {
        this.car = car;
        this.startDateTime = startDateTime;
        this.endDateTime = startDateTime.plusDays(numberOfDays);
    }
}
