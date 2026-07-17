package com.alidev.medisalud.domain.exceptions;

public class ReservationTimeNotAvailableException extends DomainException {
    public ReservationTimeNotAvailableException(String message) {
        super(message);
    }
}