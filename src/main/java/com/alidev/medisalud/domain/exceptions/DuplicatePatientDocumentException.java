package com.alidev.medisalud.domain.exceptions;

public class DuplicatePatientDocumentException extends DomainException {
    public DuplicatePatientDocumentException(String message) {
        super(message);
    }
}