package com.alidev.medisalud.application.filters;

import com.alidev.medisalud.application.dtos.request.GetReservationsRequest;
import com.alidev.medisalud.domain.entities.Reservation;
import java.util.List;

public final class ReservationFilter {
    private ReservationFilter() {}

    public static List<Reservation> apply(
            List<Reservation> reservations,
            GetReservationsRequest request
    ) {
        return reservations.stream()
                .filter(reservation ->
                        request.doctorId() == null
                                || reservation.getDoctor()
                                .getId()
                                .equals(request.doctorId())
                )
                .filter(reservation ->
                        request.patientId() == null
                                || reservation.getPatient()
                                .getId()
                                .equals(request.patientId())
                )
                .filter(reservation ->
                        request.status() == null
                                || reservation.getStatus()
                                .equals(request.status())
                )
                .filter(reservation ->
                        request.startDate() == null
                                || !reservation.getScheduledAt()
                                .toLocalDate()
                                .isBefore(request.startDate())
                )
                .filter(reservation ->
                        request.endDate() == null
                                || !reservation.getScheduledAt()
                                .toLocalDate()
                                .isAfter(request.endDate())
                )
                .toList();
    }
}