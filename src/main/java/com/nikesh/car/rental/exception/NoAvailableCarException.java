package com.nikesh.car.rental.exception;

public class NoAvailableCarException extends RuntimeException {
    public NoAvailableCarException(final String message) {
        super(message);
    }
}
