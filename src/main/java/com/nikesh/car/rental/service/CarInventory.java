package com.nikesh.car.rental.service;

import com.nikesh.car.rental.model.Car;
import com.nikesh.car.rental.model.CarType;
import com.nikesh.car.rental.model.Reservation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CarInventory {

    private final Map<CarType, List<Car>> cars = new HashMap<>();
    private final List<Reservation>  reservations = new ArrayList<>();

    public CarInventory(final Map<CarType, Integer> initialInventory) {
        for(CarType carType : initialInventory.keySet()){
            List<Car> carList = new ArrayList<>();
            for(int i=0; i<initialInventory.get(carType); i++){
                carList.add(new Car(carType.name() + "-" + i , carType));
            }
            cars.put(carType, carList);
        }
    }

    public Optional<Car> findAvailableCar(final CarType carType,
                                          final LocalDateTime startDateTime,
                                          final LocalDateTime endDateTime) {

        List<Car> availableCars = cars.getOrDefault(carType, Collections.emptyList());

        for(Car car : availableCars){
            boolean isCarBooked = reservations.stream()
                    .filter(r -> r.getCar().equals(car))
                    .anyMatch(r -> overlapsReservation(r.getStartDateTime(),
                            r.getEndDateTime(),
                            startDateTime,
                            endDateTime));

            if(!isCarBooked){
                return Optional.of(car);
            }
        }
        return Optional.empty();
    }

    public void addReservation(final Reservation reservation) {
        reservations.add(reservation);
    }

    private boolean overlapsReservation(final LocalDateTime startDateTimeFirst, final LocalDateTime endDateTimeFirst,
                                        final LocalDateTime startDateTimeSecond, final LocalDateTime endDateTimeSecond){

        return startDateTimeFirst.isBefore(endDateTimeSecond)  && startDateTimeSecond.isBefore(endDateTimeFirst);
    }

}
