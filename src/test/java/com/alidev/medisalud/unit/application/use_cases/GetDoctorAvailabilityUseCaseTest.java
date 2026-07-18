package com.alidev.medisalud.unit.application.use_cases;

import com.alidev.medisalud.application.dtos.response.GetDoctorAvailabilityResponse;
import com.alidev.medisalud.application.use_cases.GetDoctorAvailabilityUseCase;
import com.alidev.medisalud.domain.entities.Doctor;
import com.alidev.medisalud.domain.entities.Reservation;
import com.alidev.medisalud.domain.exceptions.ResourceNotFoundException;
import com.alidev.medisalud.domain.ports.infrastructure.persistence.DoctorRepositoryPort;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetDoctorAvailabilityUseCaseTest {
    @Mock
    private DoctorRepositoryPort doctorRepository;

    @Mock
    private ReservationRepositoryPort reservationRepository;

    @InjectMocks
    private GetDoctorAvailabilityUseCase useCase;

    @Test
    void shouldReturnDoctorAvailability() {
        Doctor doctor = buildDoctor();
        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));
        when(reservationRepository.findByDoctorId(doctor.getId())).thenReturn(List.of());
        GetDoctorAvailabilityResponse response =
                useCase.execute(
                        doctor.getId(),
                        LocalDate.of(2026, 7, 20),
                        LocalDate.of(2026, 7, 20)
                );
        assertThat(response).isNotNull();
        assertThat(response.doctorId()).isEqualTo(doctor.getId());
        assertThat(response.availableSlots()).hasSize(20);
    }

    @Test
    void shouldReturnDoctorAvailabilityExcludingReservedSlots() {
        Doctor doctor = buildDoctor();
        Reservation reservation =
                Reservation.builder()
                        .id(UUID.randomUUID())
                        .scheduledAt(
                                LocalDateTime.of(
                                        2026,
                                        7,
                                        20,
                                        10,
                                        0
                                )
                        )
                        .status(ReservationStatus.SCHEDULED)
                        .build();
        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));
        when(reservationRepository.findByDoctorId(doctor.getId())).thenReturn(List.of(reservation));
        GetDoctorAvailabilityResponse response =
                useCase.execute(
                        doctor.getId(),
                        LocalDate.of(2026, 7, 20),
                        LocalDate.of(2026, 7, 20)
                );

        assertThat(response.availableSlots()).hasSize(19);
    }

    @Test
    void shouldReturnDoctorAvailabilityForDateRange() {
        Doctor doctor = buildDoctor();
        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));
        when(reservationRepository.findByDoctorId(doctor.getId())).thenReturn(List.of());
        GetDoctorAvailabilityResponse response =
                useCase.execute(
                        doctor.getId(),
                        LocalDate.of(2026, 7, 20),
                        LocalDate.of(2026, 7, 21)
                );
        assertThat(response.availableSlots()).hasSize(40);
    }

    @Test
    void shouldThrowExceptionWhenDoctorDoesNotExist() {
        UUID doctorId = UUID.randomUUID();
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.empty());
        assertThatThrownBy(
                () -> useCase.execute(
                        doctorId,
                        LocalDate.of(2026, 7, 20),
                        LocalDate.of(2026, 7, 20)
                )
        ).isInstanceOf(ResourceNotFoundException.class).hasMessage("Doctor not found.");
    }

    private Doctor buildDoctor() {
        return Doctor.builder()
                .id(UUID.randomUUID())
                .fullName("Doctor Test")
                .specialty("Cardiology")
                .build();
    }
}