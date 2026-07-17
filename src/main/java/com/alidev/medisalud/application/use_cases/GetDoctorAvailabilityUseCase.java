package com.alidev.medisalud.application.use_cases;

import com.alidev.medisalud.application.dtos.response.AvailableSlotResponse;
import com.alidev.medisalud.application.dtos.response.GetDoctorAvailabilityResponse;
import com.alidev.medisalud.application.utils.AvailabilityCalculator;
import com.alidev.medisalud.domain.entities.Doctor;
import com.alidev.medisalud.domain.entities.Reservation;
import com.alidev.medisalud.domain.exceptions.ResourceNotFoundException;
import com.alidev.medisalud.domain.ports.application.GetDoctorAvailabilityPort;
import com.alidev.medisalud.domain.ports.infrastructure.persistence.DoctorRepositoryPort;
import com.alidev.medisalud.domain.ports.infrastructure.persistence.ReservationRepositoryPort;
import com.alidev.medisalud.domain.value_objects.DoctorSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetDoctorAvailabilityUseCase implements GetDoctorAvailabilityPort {
    private final DoctorRepositoryPort doctorRepository;
    private final ReservationRepositoryPort reservationRepository;

    @Override
    public GetDoctorAvailabilityResponse execute(UUID doctorId, LocalDate startDate, LocalDate endDate) {
        Doctor doctor = doctorRepository
                        .findById(doctorId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Doctor not found."
                                )
                        );
        List<Reservation> reservations = reservationRepository.findByDoctorId(doctor.getId());
        List<AvailableSlotResponse> availableSlots = AvailabilityCalculator.calculate(DoctorSchedule.defaultSchedule(), startDate, endDate, reservations);
        return new GetDoctorAvailabilityResponse(
                doctor.getId(),
                availableSlots
        );
    }
}