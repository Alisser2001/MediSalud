package com.alidev.medisalud.application.dtos.response;

import com.alidev.medisalud.domain.value_objects.ReservationStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record ScheduleReservationResponse(
        UUID reservationId,
        UUID doctorId,
        UUID patientId,
        LocalDateTime scheduledAt,
        ReservationStatus status
) {}