package com.alidev.medisalud.application.dtos.request;

import java.time.LocalDate;

public record RegisterPatientRequest(
        String fullName,
        String document,
        LocalDate birthDate,
        String phone,
        String email
) {}