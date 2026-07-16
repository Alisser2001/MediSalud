package com.alidev.medisalud.application.use_cases;

import com.alidev.medisalud.application.dtos.request.ScheduleReservationRequest;
import com.alidev.medisalud.application.dtos.response.ScheduleReservationResponse;
import com.alidev.medisalud.application.mappers.ReservationDtoMapper;
import com.alidev.medisalud.domain.entities.Doctor;
import com.alidev.medisalud.domain.entities.Patient;
import com.alidev.medisalud.domain.entities.Reservation;
import com.alidev.medisalud.domain.ports.application.ScheduleReservationPort;
import com.alidev.medisalud.domain.ports.infrastructure.persistence.DoctorRepositoryPort;
import com.alidev.medisalud.domain.ports.infrastructure.persistence.PatientRepositoryPort;
import com.alidev.medisalud.domain.ports.infrastructure.persistence.ReservationRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleReservationUseCase implements ScheduleReservationPort {
    private final DoctorRepositoryPort doctorRepository;
    private final PatientRepositoryPort patientRepository;
    private final ReservationRepositoryPort reservationRepository;

    @Override
    public ScheduleReservationResponse execute(ScheduleReservationRequest request) {
        Doctor doctor = doctorRepository.findById(request.doctorId()).orElseThrow();
        Patient patient = patientRepository.findById(request.patientId()).orElseThrow();
        Reservation reservation = ReservationDtoMapper.fromScheduleRequest(request, doctor, patient).initialize();
        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationDtoMapper.toScheduleResponse(savedReservation);
    }
}