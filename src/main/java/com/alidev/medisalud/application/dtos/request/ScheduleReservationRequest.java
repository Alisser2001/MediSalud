package com.alidev.medisalud.application.dtos.request;

import java.time.LocalDateTime;
import java.util.UUID;

public record ScheduleReservationRequest(
        UUID doctorId,
        UUID patientId,
        LocalDateTime scheduledAt
) {}