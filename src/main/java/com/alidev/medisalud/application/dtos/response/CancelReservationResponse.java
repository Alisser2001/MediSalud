package com.alidev.medisalud.application.dtos.response;

import com.alidev.medisalud.domain.value_objects.ReservationStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record CancelReservationResponse(
        UUID reservationId,
        ReservationStatus status,
        LocalDateTime cancelledAt,
        boolean penaltyApplied
) {}
