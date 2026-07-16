package com.alidev.medisalud.application.dtos.request;

import java.util.UUID;

public record CancelReservationRequest(
        UUID reservationId
) {}