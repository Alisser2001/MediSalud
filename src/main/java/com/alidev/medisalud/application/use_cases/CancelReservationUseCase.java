package com.alidev.medisalud.application.use_cases;

import com.alidev.medisalud.application.dtos.response.CancelReservationResponse;
import com.alidev.medisalud.application.mappers.ReservationDtoMapper;
import com.alidev.medisalud.domain.entities.Penalty;
import com.alidev.medisalud.domain.entities.Reservation;
import com.alidev.medisalud.domain.exceptions.ResourceNotFoundException;
import com.alidev.medisalud.domain.ports.application.CancelReservationPort;
import com.alidev.medisalud.domain.ports.infrastructure.persistence.PenaltyRepositoryPort;
import com.alidev.medisalud.domain.ports.infrastructure.persistence.ReservationRepositoryPort;
import com.alidev.medisalud.domain.value_objects.PenaltyReason;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CancelReservationUseCase implements CancelReservationPort {
    private final ReservationRepositoryPort reservationRepository;
    private final PenaltyRepositoryPort penaltyRepository;

    @Override
    public CancelReservationResponse execute(UUID reservationId) {
        Reservation reservation = reservationRepository
                                .findById(reservationId)
                                .orElseThrow(() ->
                                        new ResourceNotFoundException(
                                                "Reservation not found."
                                        )
                                );
        boolean penaltyApplied = false;
        if (reservation.getScheduledAt().isBefore(LocalDateTime.now().plusHours(24))) {
            penaltyApplied = true;
            Penalty penalty = Penalty.builder()
                                .patient(reservation.getPatient())
                                .reservation(reservation)
                                .reason(PenaltyReason.LATE_CANCELLATION)
                                .expiresAt(LocalDateTime.now().plusMonths(6))
                                .build()
                                .initialize();
            penaltyRepository.save(penalty);
        }
        reservation.cancel(LocalDateTime.now());
        Reservation updatedReservation = reservationRepository.save(reservation);
        return ReservationDtoMapper.toCancelResponse(
                updatedReservation,
                penaltyApplied
        );
    }
}