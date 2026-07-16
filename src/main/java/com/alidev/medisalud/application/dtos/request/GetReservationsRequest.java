package com.alidev.medisalud.application.dtos.request;

import com.alidev.medisalud.domain.value_objects.ReservationStatus;
import java.time.LocalDate;
import java.util.UUID;

public record GetReservationsRequest(
        UUID doctorId,
        UUID patientId,
        ReservationStatus status,
        LocalDate startDate,
        LocalDate endDate
) {}