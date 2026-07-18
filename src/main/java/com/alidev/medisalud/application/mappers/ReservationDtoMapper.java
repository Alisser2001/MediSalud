package com.alidev.medisalud.application.mappers;

import com.alidev.medisalud.application.dtos.request.ScheduleReservationRequest;
import com.alidev.medisalud.application.dtos.response.CancelReservationResponse;
import com.alidev.medisalud.application.dtos.response.ReservationSummaryResponse;
import com.alidev.medisalud.application.dtos.response.ScheduleReservationResponse;
import com.alidev.medisalud.domain.entities.Doctor;
import com.alidev.medisalud.domain.entities.Patient;
import com.alidev.medisalud.domain.entities.Reservation;

public final class ReservationDtoMapper {
    private ReservationDtoMapper() {}

    public static Reservation fromScheduleRequest(
            ScheduleReservationRequest request,
            Doctor doctor,
            Patient patient
    ) {
        if (request == null) {
            return null;
        }
        return Reservation.builder()
                .doctor(doctor)
                .patient(patient)
                .scheduledAt(request.scheduledAt())
                .build();
    }

    public static ScheduleReservationResponse toScheduleResponse(Reservation reservation) {
        if (reservation == null) {
            return null;
        }
        return new ScheduleReservationResponse(
                reservation.getId(),
                reservation.getDoctor().getId(),
                reservation.getPatient().getId(),
                reservation.getScheduledAt(),
                reservation.getStatus()
        );
    }

    public static CancelReservationResponse toCancelResponse(
            Reservation reservation,
            boolean penaltyApplied
    ) {
        if (reservation == null) {
            return null;
        }
        return new CancelReservationResponse(
                reservation.getId(),
                reservation.getStatus(),
                reservation.getCancelledAt(),
                penaltyApplied
        );
    }

    public static ReservationSummaryResponse toSummaryResponse(Reservation reservation) {
        if (reservation == null) {
            return null;
        }
        return new ReservationSummaryResponse(
                reservation.getId(),
                reservation.getDoctor().getId(),
                reservation.getDoctor().getFullName(),
                reservation.getPatient().getId(),
                reservation.getPatient().getFullName(),
                reservation.getScheduledAt(),
                reservation.getStatus()
        );
    }
}