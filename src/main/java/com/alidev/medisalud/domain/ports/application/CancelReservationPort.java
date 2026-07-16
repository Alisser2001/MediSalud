package com.alidev.medisalud.domain.ports.application;

import com.alidev.medisalud.application.dtos.request.CancelReservationRequest;
import com.alidev.medisalud.application.dtos.response.CancelReservationResponse;

public interface CancelReservationPort {
    CancelReservationResponse execute(CancelReservationRequest request);
}