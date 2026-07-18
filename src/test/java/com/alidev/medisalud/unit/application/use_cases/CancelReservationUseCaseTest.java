package com.alidev.medisalud.unit.application.use_cases;

import com.alidev.medisalud.application.dtos.response.CancelReservationResponse;
import com.alidev.medisalud.application.use_cases.CancelReservationUseCase;
import com.alidev.medisalud.domain.entities.Doctor;
import com.alidev.medisalud.domain.entities.Patient;
import com.alidev.medisalud.domain.entities.Reservation;
import com.alidev.medisalud.domain.exceptions.ReservationCancellationException;
import com.alidev.medisalud.domain.exceptions.ResourceNotFoundException;
import com.alidev.medisalud.domain.ports.infrastructure.persistence.PenaltyRepositoryPort;
import com.alidev.medisalud.domain.ports.infrastructure.persistence.ReservationRepositoryPort;
import com.alidev.medisalud.domain.value_objects.ReservationStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CancelReservationUseCaseTest {
    @Mock
    private ReservationRepositoryPort reservationRepository;

    @Mock
    private PenaltyRepositoryPort penaltyRepository;

    @InjectMocks
    private CancelReservationUseCase useCase;

    @Test
    void shouldCancelReservationWithoutPenalty() {
        Reservation reservation = buildScheduledReservation(LocalDateTime.now().plusDays(1));
        when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));
        CancelReservationResponse response = useCase.execute(reservation.getId());
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(ReservationStatus.CANCELLED);
        assertThat(response.penaltyApplied()).isFalse();
        verify(penaltyRepository, never()).save(any());
    }

    @Test
    void shouldCancelReservationWithPenalty() {
        Reservation reservation = buildScheduledReservation(LocalDateTime.now().plusMinutes(30));
        when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));
        CancelReservationResponse response = useCase.execute(reservation.getId());
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(ReservationStatus.CANCELLED);
        assertThat(response.penaltyApplied()).isTrue();
        verify(penaltyRepository).save(any());
    }

    @Test
    void shouldThrowExceptionWhenReservationDoesNotExist() {
        UUID reservationId = UUID.randomUUID();
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());
        assertThatThrownBy(
                () -> useCase.execute(reservationId)
        ).isInstanceOf(ResourceNotFoundException.class).hasMessage("Reservation not found.");
    }

    @Test
    void shouldThrowExceptionWhenReservationIsAlreadyCancelled() {
        LocalDateTime now = LocalDateTime.now();
        Reservation reservation = Reservation.builder()
                .id(UUID.randomUUID())
                .doctor(buildDoctor())
                .patient(buildPatient())
                .scheduledAt(now.plusDays(1))
                .status(ReservationStatus.CANCELLED)
                .cancelledAt(now)
                .createdAt(now)
                .updatedAt(now)
                .build();
        when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));
        assertThatThrownBy(
                () -> useCase.execute(reservation.getId())
        ).isInstanceOf(ReservationCancellationException.class).hasMessage("The reservation has already been cancelled.");
    }

    private Reservation buildScheduledReservation(LocalDateTime scheduledAt) {
        LocalDateTime now = LocalDateTime.now();
        return Reservation.builder()
                .id(UUID.randomUUID())
                .doctor(buildDoctor())
                .patient(buildPatient())
                .scheduledAt(scheduledAt)
                .status(ReservationStatus.SCHEDULED)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    private Doctor buildDoctor() {
        return Doctor.builder()
                .id(UUID.randomUUID())
                .fullName("Doctor Test")
                .specialty("Cardiology")
                .build();
    }

    private Patient buildPatient() {
        return Patient.builder()
                .id(UUID.randomUUID())
                .fullName("Patient Test")
                .document("1234567")
                .phone("1234567")
                .email("patient@test.com")
                .build();
    }
}