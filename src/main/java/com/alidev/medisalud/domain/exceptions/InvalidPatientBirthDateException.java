package com.alidev.medisalud.domain.exceptions;

public class InvalidPatientBirthDateException extends DomainException {
    public InvalidPatientBirthDateException(String message) {
        super(message);
    }
}
