package com.nikesh.car.rental.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Car {

    private final String carId;
    private final CarType carType;

}
