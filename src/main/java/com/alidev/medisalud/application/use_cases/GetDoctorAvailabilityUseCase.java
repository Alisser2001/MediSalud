package com.alidev.medisalud.application.use_cases;

import com.alidev.medisalud.application.dtos.request.GetDoctorAvailabilityRequest;
import com.alidev.medisalud.application.dtos.response.AvailableSlotResponse;
import com.alidev.medisalud.application.dtos.response.GetDoctorAvailabilityResponse;
import com.alidev.medisalud.application.utils.AvailabilityCalculator;
import com.alidev.medisalud.domain.entities.Doctor;
import com.alidev.medisalud.domain.entities.Reservation;
import com.alidev.medisalud.domain.ports.application.GetDoctorAvailabilityPort;
import com.alidev.medisalud.domain.ports.infrastructure.persistence.DoctorRepositoryPort;
import com.alidev.medisalud.domain.ports.infrastructure.persistence.ReservationRepositoryPort;
import com.alidev.medisalud.domain.value_objects.DoctorSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetDoctorAvailabilityUseCase implements GetDoctorAvailabilityPort {
    private final DoctorRepositoryPort doctorRepository;
    private final ReservationRepositoryPort reservationRepository;

    @Override
    public GetDoctorAvailabilityResponse execute(GetDoctorAvailabilityRequest request) {
        Doctor doctor = doctorRepository.findById(request.doctorId()).orElseThrow();
        List<Reservation> reservations = reservationRepository.findByDoctorId(doctor.getId());
        List<AvailableSlotResponse> availableSlots = AvailabilityCalculator.calculate(
                                                            DoctorSchedule.defaultSchedule(),
                                                            request.startDate(),
                                                            request.endDate(),
                                                            reservations
                                                     );
        return new GetDoctorAvailabilityResponse(
                doctor.getId(),
                availableSlots
        );
    }
}