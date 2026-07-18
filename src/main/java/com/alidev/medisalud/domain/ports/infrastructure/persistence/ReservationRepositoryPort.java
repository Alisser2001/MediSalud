package com.alidev.medisalud.domain.ports.infrastructure.persistence;

import com.alidev.medisalud.domain.entities.Reservation;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRepositoryPort {
    Reservation save(Reservation reservation);
    Optional<Reservation> findById(UUID reservationId);
    List<Reservation> findAll();
    List<Reservation> findByPatientId(UUID patientId);
    List<Reservation> findByDoctorId(UUID doctorId);
}
