package com.alidev.medisalud.application.dtos.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public record ScheduleReservationRequest(
        UUID doctorId,
        UUID patientId,

        @NotNull(message = "Scheduled date and time is required.")
        LocalDateTime scheduledAt
) {}