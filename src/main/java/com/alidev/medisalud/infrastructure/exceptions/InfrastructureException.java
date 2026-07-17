package com.alidev.medisalud.infrastructure.exceptions;

public abstract class InfrastructureException extends RuntimeException {
    protected InfrastructureException(String message, Throwable cause) {
        super(message, cause);
    }
}