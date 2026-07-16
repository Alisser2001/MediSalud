package com.alidev.medisalud.domain.exceptions;

public class ReservationCancellationException extends RuntimeException {
    public ReservationCancellationException(String message) {
        super(message);
    }
}
