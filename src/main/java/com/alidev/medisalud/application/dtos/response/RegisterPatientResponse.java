package com.alidev.medisalud.application.dtos.response;

import java.util.UUID;

public record RegisterPatientResponse(
        UUID id,
        String fullName,
        String document,
        String phone,
        String email
) {}