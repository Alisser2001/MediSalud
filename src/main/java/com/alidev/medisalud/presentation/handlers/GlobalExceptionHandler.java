package com.alidev.medisalud.presentation.handlers;

import com.alidev.medisalud.domain.exceptions.DomainException;
import com.alidev.medisalud.domain.exceptions.ResourceNotFoundException;
import com.alidev.medisalud.infrastructure.exceptions.InfrastructureException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        return buildResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomain(DomainException ex) {
        return buildResponse(
                HttpStatus.CONFLICT,
                ex.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("Validation error.");
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                message
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
    }

    @ExceptionHandler(InfrastructureException.class)
    public ResponseEntity<ErrorResponse> handleInfrastructure(InfrastructureException ex) {
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An infrastructure error occurred."
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Unexpected error."
        );
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message) {
        return ResponseEntity
                .status(status)
                .body(
                        new ErrorResponse(
                                status.value(),
                                status.getReasonPhrase(),
                                message,
                                LocalDateTime.now()
                        )
                );
    }
}