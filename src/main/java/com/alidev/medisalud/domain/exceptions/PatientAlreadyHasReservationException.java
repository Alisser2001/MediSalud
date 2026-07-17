package com.alidev.medisalud.domain.exceptions;

public class PatientAlreadyHasReservationException extends DomainException {
    public PatientAlreadyHasReservationException(String message) {
        super(message);
    }
}