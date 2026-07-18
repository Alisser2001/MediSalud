package com.alidev.medisalud.domain.ports.application;

import com.alidev.medisalud.application.dtos.response.CancelReservationResponse;
import java.util.UUID;

public interface CancelReservationPort {
    CancelReservationResponse execute(UUID reservationId);
}