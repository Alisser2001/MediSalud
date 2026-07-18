package com.alidev.medisalud.domain.exceptions;

public abstract class DomainException extends RuntimeException {
    protected DomainException(String message) {
        super(message);
    }
}