package com.alidev.medisalud.application.dtos.request;

import java.time.LocalDate;
import java.util.UUID;

public record GetDoctorAvailabilityRequest(
        UUID doctorId,
        LocalDate startDate,
        LocalDate endDate
) {}