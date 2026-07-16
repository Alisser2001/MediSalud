package com.alidev.medisalud.application.dtos.response;

import com.alidev.medisalud.domain.value_objects.ReservationStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record ReservationSummaryResponse(
        UUID reservationId,
        UUID doctorId,
        String doctorName,
        UUID patientId,
        String patientName,
        LocalDateTime scheduledAt,
        ReservationStatus status
) {}
