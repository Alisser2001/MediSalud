package com.alidev.medisalud.application.use_cases;

import com.alidev.medisalud.application.dtos.request.ScheduleReservationRequest;
import com.alidev.medisalud.application.dtos.response.ScheduleReservationResponse;
import com.alidev.medisalud.application.mappers.ReservationDtoMapper;
import com.alidev.medisalud.domain.entities.Doctor;
import com.alidev.medisalud.domain.entities.Patient;
import com.alidev.medisalud.domain.entities.Reservation;
import com.alidev.medisalud.domain.exceptions.*;
import com.alidev.medisalud.domain.ports.application.ScheduleReservationPort;
import com.alidev.medisalud.domain.ports.infrastructure.persistence.DoctorRepositoryPort;
import com.alidev.medisalud.domain.ports.infrastructure.persistence.PatientRepositoryPort;
import com.alidev.medisalud.domain.ports.infrastructure.persistence.PenaltyRepositoryPort;
import com.alidev.medisalud.domain.ports.infrastructure.persistence.ReservationRepositoryPort;
import com.alidev.medisalud.domain.value_objects.DoctorSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ScheduleReservationUseCase implements ScheduleReservationPort {
    private final DoctorRepositoryPort doctorRepository;
    private final PatientRepositoryPort patientRepository;
    private final ReservationRepositoryPort reservationRepository;
    private final PenaltyRepositoryPort penaltyRepository;

    @Override
    public ScheduleReservationResponse execute(ScheduleReservationRequest request) {
        Doctor doctor = doctorRepository
                        .findById(request.doctorId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Doctor not found."
                                )
                        );
        Patient patient = patientRepository
                        .findById(request.patientId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Patient not found."
                                )
                        );
        DoctorSchedule schedule = DoctorSchedule.defaultSchedule();
        if (!schedule.isValidSlot(request.scheduledAt())) {
            throw new InvalidReservationScheduleException(
                    "Invalid reservation schedule."
            );
        }
        boolean doctorBusy = reservationRepository
                            .findByDoctorId(doctor.getId())
                            .stream()
                            .filter(r -> !r.isCancelled())
                            .anyMatch(r ->
                                    r.getScheduledAt().equals(request.scheduledAt())
                            );
        if (doctorBusy) {
            throw new ReservationTimeNotAvailableException(
                    "Doctor already has a reservation in that time slot."
            );
        }
        boolean patientConflict = reservationRepository
                                .findByPatientId(patient.getId())
                                .stream()
                                .filter(r -> !r.isCancelled())
                                .anyMatch(r ->
                                        r.getDoctor().getId().equals(doctor.getId()) && r.getScheduledAt().equals(request.scheduledAt())
                                );
        if (patientConflict) {
            throw new PatientAlreadyHasReservationException(
                    "Patient already has a reservation with this doctor in that time slot."
            );
        }
        long activePenalties = penaltyRepository
                            .findByPatientId(patient.getId())
                            .stream()
                            .filter(p -> p.getExpiresAt().isAfter(LocalDateTime.now()))
                            .count();
        if (activePenalties >= 3) {
            throw new PatientBlockedException(
                    "Patient has 3 or more active penalties."
            );
        }
        Reservation reservation = ReservationDtoMapper.fromScheduleRequest(request, doctor, patient).initialize();
        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationDtoMapper.toScheduleResponse(savedReservation);
    }
}