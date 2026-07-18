package com.alidev.medisalud.unit.application.use_cases;

import com.alidev.medisalud.application.dtos.request.ScheduleReservationRequest;
import com.alidev.medisalud.application.dtos.response.ScheduleReservationResponse;
import com.alidev.medisalud.application.use_cases.ScheduleReservationUseCase;
import com.alidev.medisalud.domain.entities.Doctor;
import com.alidev.medisalud.domain.entities.Patient;
import com.alidev.medisalud.domain.entities.Penalty;
import com.alidev.medisalud.domain.entities.Reservation;
import com.alidev.medisalud.domain.exceptions.*;
import com.alidev.medisalud.domain.ports.infrastructure.persistence.DoctorRepositoryPort;
import com.alidev.medisalud.domain.ports.infrastructure.persistence.PatientRepositoryPort;
import com.alidev.medisalud.domain.ports.infrastructure.persistence.PenaltyRepositoryPort;
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
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleReservationUseCaseTest {
    @Mock
    private DoctorRepositoryPort doctorRepository;

    @Mock
    private PatientRepositoryPort patientRepository;

    @Mock
    private ReservationRepositoryPort reservationRepository;

    @Mock
    private PenaltyRepositoryPort penaltyRepository;

    @InjectMocks
    private ScheduleReservationUseCase useCase;

    @Test
    void shouldScheduleReservationSuccessfully() {
        Doctor doctor = buildDoctor();
        Patient patient = buildPatient(null);
        LocalDateTime scheduledAt = LocalDateTime.of(2026, 7, 20, 10, 0);
        ScheduleReservationRequest request =
                new ScheduleReservationRequest(
                        doctor.getId(),
                        patient.getId(),
                        scheduledAt
                );
        Reservation savedReservation =
                Reservation.builder()
                        .id(UUID.randomUUID())
                        .doctor(doctor)
                        .patient(patient)
                        .scheduledAt(scheduledAt)
                        .status(ReservationStatus.SCHEDULED)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();
        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        when(reservationRepository.findByDoctorId(doctor.getId())).thenReturn(List.of());
        when(reservationRepository.findByPatientId(patient.getId())).thenReturn(List.of());
        when(penaltyRepository.findByPatientId(patient.getId())).thenReturn(List.of());
        when(reservationRepository.save(any(Reservation.class))).thenReturn(savedReservation);
        ScheduleReservationResponse response = useCase.execute(request);
        assertThat(response).isNotNull();
        assertThat(response.reservationId()).isEqualTo(savedReservation.getId());
        assertThat(response.status()).isEqualTo(ReservationStatus.SCHEDULED);
    }

    @Test
    void shouldScheduleReservationForPatientWithoutBirthDate() {
        Doctor doctor = buildDoctor();
        Patient patient = buildPatient(null);
        executeSuccessfulReservation(doctor, patient);
    }

    @Test
    void shouldScheduleReservationForPatientWithValidBirthDate() {
        Doctor doctor = buildDoctor();
        Patient patient = buildPatient(LocalDate.of(1995, 5, 10));
        executeSuccessfulReservation(doctor, patient);
    }

    @Test
    void shouldThrowExceptionWhenDoctorDoesNotExist() {
        UUID doctorId = UUID.randomUUID();
        ScheduleReservationRequest request =
                new ScheduleReservationRequest(
                        doctorId,
                        UUID.randomUUID(),
                        LocalDateTime.of(2026, 7, 20, 10, 0)
                );
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.empty());
        assertThatThrownBy(
                () -> useCase.execute(request)
        ).isInstanceOf(ResourceNotFoundException.class).hasMessage("Doctor not found.");
    }

    @Test
    void shouldThrowExceptionWhenPatientDoesNotExist() {
        Doctor doctor = buildDoctor();
        UUID patientId = UUID.randomUUID();
        ScheduleReservationRequest request =
                new ScheduleReservationRequest(
                        doctor.getId(),
                        patientId,
                        LocalDateTime.of(2026, 7, 20, 10, 0)
                );
        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());
        assertThatThrownBy(
                () -> useCase.execute(request)
        ).isInstanceOf(ResourceNotFoundException.class).hasMessage("Patient not found.");
    }

    @Test
    void shouldThrowExceptionWhenPatientBirthDateIsFuture() {
        Doctor doctor = buildDoctor();
        Patient patient = buildPatient(LocalDate.now().plusDays(1));
        ScheduleReservationRequest request =
                new ScheduleReservationRequest(
                        doctor.getId(),
                        patient.getId(),
                        LocalDateTime.of(2026, 7, 20, 10, 0)
                );
        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        assertThatThrownBy(
                () -> useCase.execute(request)
        ).isInstanceOf(InvalidPatientBirthDateException.class).hasMessage("Birth date cannot be in the future.");
    }

    @Test
    void shouldThrowExceptionWhenReservationIsOnSunday() {
        assertInvalidSchedule(
                LocalDateTime.of(2026, 7, 19, 10, 0)
        );
    }

    @Test
    void shouldThrowExceptionWhenReservationIsBeforeOpeningHours() {
        assertInvalidSchedule(
                LocalDateTime.of(2026, 7, 20, 7, 30)
        );
    }

    @Test
    void shouldThrowExceptionWhenReservationIsAfterClosingHoursOnWeekday() {
        assertInvalidSchedule(
                LocalDateTime.of(2026, 7, 20, 18, 0)
        );
    }

    @Test
    void shouldThrowExceptionWhenReservationIsAfterClosingHoursOnSaturday() {
        assertInvalidSchedule(
                LocalDateTime.of(2026, 7, 18, 13, 0)
        );
    }

    @Test
    void shouldThrowExceptionWhenReservationSlotIsNotThirtyMinutes() {
        assertInvalidSchedule(
                LocalDateTime.of(2026, 7, 20, 10, 15)
        );
    }

    @Test
    void shouldThrowExceptionWhenDoctorAlreadyHasReservationInSameSlot() {
        Doctor doctor = buildDoctor();
        Patient patient = buildPatient(null);
        LocalDateTime scheduledAt = LocalDateTime.of(2026, 7, 20, 10, 0);
        Reservation reservation = buildReservation(doctor, patient, scheduledAt);
        ScheduleReservationRequest request = new ScheduleReservationRequest(doctor.getId(), patient.getId(), scheduledAt);
        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        when(reservationRepository.findByDoctorId(doctor.getId())).thenReturn(List.of(reservation));
        assertThatThrownBy(
                () -> useCase.execute(request)
        ).isInstanceOf(ReservationTimeNotAvailableException.class).hasMessage("Doctor already has a reservation in that time slot.");
    }

    @Test
    void shouldThrowExceptionWhenPatientAlreadyHasReservationWithSameDoctorInSameSlot() {
        Doctor doctor = buildDoctor();
        Patient patient = buildPatient(null);
        LocalDateTime scheduledAt = LocalDateTime.of(2026, 7, 20, 10, 0);
        Reservation reservation = buildReservation(doctor, patient, scheduledAt);
        ScheduleReservationRequest request = new ScheduleReservationRequest(doctor.getId(), patient.getId(), scheduledAt);
        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        when(reservationRepository.findByDoctorId(doctor.getId())).thenReturn(List.of());
        when(reservationRepository.findByPatientId(patient.getId())).thenReturn(List.of(reservation));
        assertThatThrownBy(
                () -> useCase.execute(request)
        ).isInstanceOf(PatientAlreadyHasReservationException.class).hasMessage("Patient already has a reservation with this doctor in that time slot.");
    }

    @Test
    void shouldThrowExceptionWhenPatientHasThreeActivePenalties() {
        assertPatientBlocked(3);
    }

    @Test
    void shouldThrowExceptionWhenPatientHasMoreThanThreeActivePenalties() {
        assertPatientBlocked(4);
    }

    private void assertInvalidSchedule(LocalDateTime scheduledAt) {
        Doctor doctor = buildDoctor();
        Patient patient = buildPatient(null);
        ScheduleReservationRequest request = new ScheduleReservationRequest(doctor.getId(), patient.getId(), scheduledAt);
        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        assertThatThrownBy(
                () -> useCase.execute(request)
        ).isInstanceOf(InvalidReservationScheduleException.class).hasMessage("Invalid reservation schedule.");
    }

    private void assertPatientBlocked(int penalties) {
        Doctor doctor = buildDoctor();
        Patient patient = buildPatient(null);
        ScheduleReservationRequest request = new ScheduleReservationRequest(doctor.getId(), patient.getId(), LocalDateTime.of(2026, 7, 20, 10, 0));
        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        when(reservationRepository.findByDoctorId(doctor.getId())).thenReturn(List.of());
        when(reservationRepository.findByPatientId(patient.getId())).thenReturn(List.of());
        when(penaltyRepository.findByPatientId(patient.getId()))
                .thenReturn(
                        java.util.stream.IntStream.range(0, penalties)
                                .mapToObj(i -> buildActivePenalty(patient))
                                .toList()
                );
        assertThatThrownBy(
                () -> useCase.execute(request)
        ).isInstanceOf(PatientBlockedException.class).hasMessage("Patient has 3 or more active penalties.");
    }

    private void executeSuccessfulReservation(
            Doctor doctor,
            Patient patient
    ) {
        LocalDateTime scheduledAt = LocalDateTime.of(2026, 7, 20, 10, 0);
        ScheduleReservationRequest request = new ScheduleReservationRequest(doctor.getId(), patient.getId(), scheduledAt);
        Reservation savedReservation = buildReservation(doctor, patient, scheduledAt);
        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        when(reservationRepository.findByDoctorId(doctor.getId())).thenReturn(List.of());
        when(reservationRepository.findByPatientId(patient.getId())).thenReturn(List.of());
        when(penaltyRepository.findByPatientId(patient.getId())).thenReturn(List.of());
        when(reservationRepository.save(any(Reservation.class))).thenReturn(savedReservation);
        ScheduleReservationResponse response = useCase.execute(request);
        assertThat(response).isNotNull();
    }

    private Doctor buildDoctor() {
        return Doctor.builder()
                .id(UUID.randomUUID())
                .fullName("Doctor Test")
                .specialty("Cardiology")
                .build();
    }

    private Patient buildPatient(LocalDate birthDate) {
        return Patient.builder()
                .id(UUID.randomUUID())
                .fullName("Patient Test")
                .document("1234567")
                .birthDate(birthDate)
                .phone("1234567")
                .email("patient@test.com")
                .build();
    }

    private Reservation buildReservation(
            Doctor doctor,
            Patient patient,
            LocalDateTime scheduledAt
    ) {
        return Reservation.builder()
                .id(UUID.randomUUID())
                .doctor(doctor)
                .patient(patient)
                .scheduledAt(scheduledAt)
                .status(ReservationStatus.SCHEDULED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private Penalty buildActivePenalty(Patient patient) {
        return Penalty.builder()
                .id(UUID.randomUUID())
                .patient(patient)
                .expiresAt(LocalDateTime.now().plusDays(10))
                .build();
    }
}