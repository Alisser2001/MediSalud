package com.alidev.medisalud.domain.exceptions;

public class InvalidReservationScheduleException extends DomainException {
    public InvalidReservationScheduleException(String message) {
        super(message);
    }
}