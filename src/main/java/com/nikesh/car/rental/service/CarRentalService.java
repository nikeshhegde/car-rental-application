package com.nikesh.car.rental.service;

import com.nikesh.car.rental.exception.NoAvailableCarException;
import com.nikesh.car.rental.model.Car;
import com.nikesh.car.rental.model.CarType;
import com.nikesh.car.rental.model.Reservation;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
@ToString
public class CarRentalService {

    private final CarInventory carInventory;

    public Reservation reserveCar(final CarType carType,
                                  final LocalDateTime startDateTime,
                                  final int numberOfDays) {

        if(numberOfDays <= 0){
            throw new IllegalArgumentException("Number of days must be greater than zero");
        }

        if(startDateTime.isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("Start date cannot be in past");
        }

        final LocalDateTime endDateTime = startDateTime.plusDays(numberOfDays);

        final Car car = carInventory.findAvailableCar(carType, startDateTime, endDateTime)
                .orElseThrow(() -> new NoAvailableCarException("No car available between " + startDateTime + " and " + endDateTime));

        final Reservation reservation = new Reservation(car, startDateTime, numberOfDays);
        carInventory.addReservation(reservation);

        return reservation;
    }
}
