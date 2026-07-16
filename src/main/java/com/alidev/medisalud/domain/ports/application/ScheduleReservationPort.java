package com.alidev.medisalud.domain.ports.application;

import com.alidev.medisalud.application.dtos.request.ScheduleReservationRequest;
import com.alidev.medisalud.application.dtos.response.ScheduleReservationResponse;

public interface ScheduleReservationPort {
    ScheduleReservationResponse execute(ScheduleReservationRequest request);
}