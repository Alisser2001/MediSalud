package com.alidev.medisalud.application.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterDoctorRequest(
        @NotBlank(message = "Full name is required.")
        @Size(min = 3, max = 100, message = "Full name must be between 3 and 100 characters.")
        String fullName,

        @NotBlank(message = "Specialty is required.")
        String specialty,

        @Size(min = 7, message = "Phone must contain at least 7 digits.")
        String phone,

        @Email(message = "Invalid email format.")
        String email
) {}