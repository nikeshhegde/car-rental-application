package com.nikesh.car.rental.test.service;

import com.nikesh.car.rental.exception.NoAvailableCarException;
import com.nikesh.car.rental.model.CarType;
import com.nikesh.car.rental.model.Reservation;
import com.nikesh.car.rental.service.CarInventory;
import com.nikesh.car.rental.service.CarRentalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.nikesh.car.rental.model.CarType.SEDAN;
import static com.nikesh.car.rental.model.CarType.SUV;
import static com.nikesh.car.rental.model.CarType.VAN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CarRentalServiceTest {

    private CarRentalService carRentalService;

    @BeforeEach
    void setUp() {
        //Initialize car inventory with limited cars
        Map<CarType, Integer> carInventoryMap = new HashMap<>();
        carInventoryMap.put(SEDAN,3);
        carInventoryMap.put(SUV,2);
        carInventoryMap.put(VAN,1);

        CarInventory carInventory = new CarInventory(carInventoryMap);
        carRentalService = new CarRentalService(carInventory);
    }

    @Test
    void testReserveSedanSuccessfully(){

        //Initialize days
        LocalDateTime startDateTime = LocalDateTime.now().plusDays(1);
        int numberOfDays = 3;

        //Reserve Sedan car for 3 days
        Reservation reservation = carRentalService.reserveCar(SEDAN, startDateTime, numberOfDays);

        assertNotNull(reservation);
        assertEquals(SEDAN, reservation.getCar().getCarType());
        assertEquals(startDateTime.plusDays(numberOfDays), reservation.getEndDateTime());
    }

    @Test
    void testReserveSedanFailsWhenReservationFull(){

        //Initialize days
        LocalDateTime startDateTime = LocalDateTime.now().plusDays(1);
        int numberOfDays = 2;

        //Reserve all 3 Sedans
        carRentalService.reserveCar(SEDAN, startDateTime, numberOfDays);
        carRentalService.reserveCar(SEDAN, startDateTime, numberOfDays);
        carRentalService.reserveCar(SEDAN, startDateTime, numberOfDays);

        //One more attempt should fail
        assertThrows(NoAvailableCarException.class,
                () -> carRentalService.reserveCar(SEDAN, startDateTime, numberOfDays));
    }

    @Test
    void testOverlappingReservationsFail() {

        //Initialize
        LocalDateTime startDateTime = LocalDateTime.now().plusDays(1);
        int numberOfDays = 3;

        // Reserve SUV 1
        carRentalService.reserveCar(SUV, startDateTime, numberOfDays);

        // Overlapping reservation should succeed if another SUV is free
        carRentalService.reserveCar(SUV, startDateTime.plusDays(1), numberOfDays);

        // Third overlapping attempt exceeds 2 SUVs, reserving third SUV should fail
        assertThrows(NoAvailableCarException.class,
                () -> carRentalService.reserveCar(SUV, startDateTime.plusDays(1), numberOfDays));
    }

    @Test
    void testDifferentCarTypes() {
        //Initialize
        LocalDateTime startDateTime = LocalDateTime.now().plusDays(1);
        int numberOfDays = 2;

        // Reserve all vans
        carRentalService.reserveCar(VAN, startDateTime, numberOfDays);

        // Sedan should still be available
        Reservation sedanReservation = carRentalService.reserveCar(SEDAN, startDateTime, numberOfDays);

        assertNotNull(sedanReservation);
        assertEquals(SEDAN, sedanReservation.getCar().getCarType());
    }

    @Test
    void testReservationWithNegativeDaysFail() {

        LocalDateTime startDateTime = LocalDateTime.now();
        int numberOfDays = -1;

        assertThrows(IllegalArgumentException.class,
                () -> carRentalService.reserveCar(SEDAN, startDateTime, numberOfDays));
    }

    @Test
    void testReservationInPastFail() {
        LocalDateTime startDateTime = LocalDateTime.now().minusDays(1);
        int numberOfDays = 2;

        assertThrows(IllegalArgumentException.class,
                () -> carRentalService.reserveCar(SEDAN, startDateTime, numberOfDays));
    }


}
