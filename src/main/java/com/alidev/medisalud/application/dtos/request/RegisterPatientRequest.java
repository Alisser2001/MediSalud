package com.alidev.medisalud.application.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record RegisterPatientRequest(
        @NotBlank(message = "Full name is required.")
        @Size(min = 3, max = 100, message = "Full name must be between 3 and 100 characters.")
        String fullName,

        @NotBlank(message = "Document is required.")
        @Size(min = 7, message = "Document must contain at least 7 characters.")
        String document,

        LocalDate birthDate,

        @NotBlank(message = "Phone is required.")
        @Size(min = 7, message = "Phone must contain at least 7 digits.")
        String phone,

        @NotBlank(message = "Email is required.")
        @Email(message = "Invalid email format.")
        String email
) {}