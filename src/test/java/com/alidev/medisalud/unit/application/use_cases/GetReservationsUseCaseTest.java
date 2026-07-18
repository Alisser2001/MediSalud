package com.alidev.medisalud.unit.application.use_cases;

import com.alidev.medisalud.application.dtos.request.GetReservationsRequest;
import com.alidev.medisalud.application.dtos.response.ReservationSummaryResponse;
import com.alidev.medisalud.application.use_cases.GetReservationsUseCase;
import com.alidev.medisalud.domain.entities.Doctor;
import com.alidev.medisalud.domain.entities.Patient;
import com.alidev.medisalud.domain.entities.Reservation;
import com.alidev.medisalud.domain.ports.infrastructure.persistence.ReservationRepositoryPort;
import com.alidev.medisalud.domain.value_objects.ReservationStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetReservationsUseCaseTest {
    @Mock
    private ReservationRepositoryPort reservationRepository;

    @InjectMocks
    private GetReservationsUseCase useCase;

    @Test
    void shouldReturnAllReservations() {
        Reservation reservation1 = buildReservation(
                ReservationStatus.SCHEDULED,
                LocalDateTime.of(2026, 7, 20, 10, 0)
        );
        Reservation reservation2 = buildReservation(
                ReservationStatus.CANCELLED,
                LocalDateTime.of(2026, 7, 21, 10, 0)
        );
        when(reservationRepository.findAll()).thenReturn(List.of(reservation1, reservation2));
        GetReservationsRequest request =
                new GetReservationsRequest(
                        null,
                        null,
                        null,
                        null,
                        null
                );
        List<ReservationSummaryResponse> response = useCase.execute(request);
        assertThat(response).hasSize(2);
    }

    @Test
    void shouldFilterReservationsByDoctor() {
        UUID doctorId = UUID.randomUUID();
        Reservation matchingReservation =
                buildReservation(
                        doctorId,
                        UUID.randomUUID(),
                        ReservationStatus.SCHEDULED,
                        LocalDateTime.of(2026, 7, 20, 10, 0)
                );
        Reservation otherReservation =
                buildReservation(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        ReservationStatus.SCHEDULED,
                        LocalDateTime.of(2026, 7, 20, 11, 0)
                );
        when(reservationRepository.findAll()).thenReturn(List.of(matchingReservation, otherReservation));
        GetReservationsRequest request =
                new GetReservationsRequest(
                        doctorId,
                        null,
                        null,
                        null,
                        null
                );
        List<ReservationSummaryResponse> response = useCase.execute(request);
        assertThat(response).hasSize(1);
        assertThat(response.getFirst().doctorId()).isEqualTo(doctorId);
    }

    @Test
    void shouldFilterReservationsByPatient() {
        UUID patientId = UUID.randomUUID();
        Reservation matchingReservation =
                buildReservation(
                        UUID.randomUUID(),
                        patientId,
                        ReservationStatus.SCHEDULED,
                        LocalDateTime.of(2026, 7, 20, 10, 0)
                );
        Reservation otherReservation =
                buildReservation(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        ReservationStatus.SCHEDULED,
                        LocalDateTime.of(2026, 7, 20, 11, 0)
                );
        when(reservationRepository.findAll()).thenReturn(List.of(matchingReservation, otherReservation));
        GetReservationsRequest request =
                new GetReservationsRequest(
                        null,
                        patientId,
                        null,
                        null,
                        null
                );
        List<ReservationSummaryResponse> response = useCase.execute(request);
        assertThat(response).hasSize(1);
        assertThat(response.getFirst().patientId()).isEqualTo(patientId);
    }

    @Test
    void shouldFilterReservationsByStatusScheduled() {
        Reservation scheduledReservation =
                buildReservation(
                        ReservationStatus.SCHEDULED,
                        LocalDateTime.of(2026, 7, 20, 10, 0)
                );
        Reservation cancelledReservation =
                buildReservation(
                        ReservationStatus.CANCELLED,
                        LocalDateTime.of(2026, 7, 20, 11, 0)
                );
        when(reservationRepository.findAll()).thenReturn(List.of(scheduledReservation, cancelledReservation));
        GetReservationsRequest request =
                new GetReservationsRequest(
                        null,
                        null,
                        ReservationStatus.SCHEDULED,
                        null,
                        null
                );
        List<ReservationSummaryResponse> response = useCase.execute(request);
        assertThat(response).hasSize(1);
        assertThat(response.getFirst().status()).isEqualTo(ReservationStatus.SCHEDULED);
    }

    @Test
    void shouldFilterReservationsByStatusCancelled() {
        Reservation scheduledReservation =
                buildReservation(
                        ReservationStatus.SCHEDULED,
                        LocalDateTime.of(2026, 7, 20, 10, 0)
                );

        Reservation cancelledReservation =
                buildReservation(
                        ReservationStatus.CANCELLED,
                        LocalDateTime.of(2026, 7, 20, 11, 0)
                );
        when(reservationRepository.findAll()).thenReturn(List.of(scheduledReservation, cancelledReservation));
        GetReservationsRequest request =
                new GetReservationsRequest(
                        null,
                        null,
                        ReservationStatus.CANCELLED,
                        null,
                        null
                );
        List<ReservationSummaryResponse> response = useCase.execute(request);
        assertThat(response).hasSize(1);
        assertThat(response.getFirst().status()).isEqualTo(ReservationStatus.CANCELLED);
    }

    @Test
    void shouldFilterReservationsByDateRange() {
        Reservation reservationInRange =
                buildReservation(
                        ReservationStatus.SCHEDULED,
                        LocalDateTime.of(2026, 7, 20, 10, 0)
                );
        Reservation reservationOutOfRange =
                buildReservation(
                        ReservationStatus.SCHEDULED,
                        LocalDateTime.of(2026, 8, 20, 10, 0)
                );
        when(reservationRepository.findAll()).thenReturn(List.of(reservationInRange, reservationOutOfRange));
        GetReservationsRequest request =
                new GetReservationsRequest(
                        null,
                        null,
                        null,
                        LocalDate.of(2026, 7, 1),
                        LocalDate.of(2026, 7, 31)
                );
        List<ReservationSummaryResponse> response = useCase.execute(request);
        assertThat(response).hasSize(1);
    }

    @Test
    void shouldApplyMultipleFiltersSimultaneously() {
        UUID doctorId = UUID.randomUUID();
        UUID patientId = UUID.randomUUID();
        Reservation matchingReservation =
                buildReservation(
                        doctorId,
                        patientId,
                        ReservationStatus.SCHEDULED,
                        LocalDateTime.of(2026, 7, 20, 10, 0)
                );
        Reservation otherReservation =
                buildReservation(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        ReservationStatus.CANCELLED,
                        LocalDateTime.of(2026, 8, 20, 10, 0)
                );
        when(reservationRepository.findAll()).thenReturn(List.of(matchingReservation, otherReservation));
        GetReservationsRequest request =
                new GetReservationsRequest(
                        doctorId,
                        patientId,
                        ReservationStatus.SCHEDULED,
                        LocalDate.of(2026, 7, 1),
                        LocalDate.of(2026, 7, 31)
                );
        List<ReservationSummaryResponse> response = useCase.execute(request);
        assertThat(response).hasSize(1);
        ReservationSummaryResponse reservation = response.getFirst();
        assertThat(reservation.doctorId()).isEqualTo(doctorId);
        assertThat(reservation.patientId()).isEqualTo(patientId);
        assertThat(reservation.status()).isEqualTo(ReservationStatus.SCHEDULED);
    }

    private Reservation buildReservation(
            ReservationStatus status,
            LocalDateTime scheduledAt
    ) {
        return buildReservation(
                UUID.randomUUID(),
                UUID.randomUUID(),
                status,
                scheduledAt
        );
    }

    private Reservation buildReservation(
            UUID doctorId,
            UUID patientId,
            ReservationStatus status,
            LocalDateTime scheduledAt
    ) {
        Doctor doctor = Doctor.builder()
                .id(doctorId)
                .fullName("Doctor Test")
                .specialty("Cardiology")
                .build();
        Patient patient = Patient.builder()
                .id(patientId)
                .fullName("Patient Test")
                .document("1234567")
                .phone("1234567")
                .email("patient@test.com")
                .build();
        return Reservation.builder()
                .id(UUID.randomUUID())
                .doctor(doctor)
                .patient(patient)
                .scheduledAt(scheduledAt)
                .status(status)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}