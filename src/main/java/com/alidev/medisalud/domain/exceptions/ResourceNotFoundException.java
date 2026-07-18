package com.alidev.medisalud.domain.exceptions;

public class ResourceNotFoundException extends DomainException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}