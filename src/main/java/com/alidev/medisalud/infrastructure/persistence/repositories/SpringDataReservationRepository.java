package com.alidev.medisalud.infrastructure.persistence.repositories;

import com.alidev.medisalud.infrastructure.persistence.entities.ReservationJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface SpringDataReservationRepository extends JpaRepository<ReservationJpaEntity, UUID> {
    List<ReservationJpaEntity> findByDoctorId(UUID doctorId);
    List<ReservationJpaEntity> findByPatientId(UUID patientId);
}