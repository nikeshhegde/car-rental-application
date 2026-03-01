package com.nikesh.car.rental.model;

import lombok.Getter;

@Getter
public enum CarType {
    SEDAN("SEDAN"),
    SUV("SUV"),
    VAN("VAN");

    private final String displayName;

    CarType(String displayName) {
        this.displayName = displayName;
    }
}
