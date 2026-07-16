package com.alidev.medisalud.application.dtos.request;

public record RegisterDoctorRequest(
        String fullName,
        String specialty,
        String phone,
        String email
) {}