package com.alidev.medisalud.domain.exceptions;

public class ReservationCancellationException extends DomainException {
    public ReservationCancellationException(String message) {
        super(message);
    }
}