package com.alidev.medisalud.application.dtos.response;

import java.util.UUID;

public record RegisterDoctorResponse(
        UUID id,
        String fullName,
        String specialty,
        String phone,
        String email
) {}