package com.alidev.medisalud.domain.ports.application;

import com.alidev.medisalud.application.dtos.request.GetReservationsRequest;
import com.alidev.medisalud.application.dtos.response.ReservationSummaryResponse;
import java.util.List;

public interface GetReservationsPort {
    List<ReservationSummaryResponse> execute(GetReservationsRequest request);
}