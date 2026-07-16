package com.alidev.medisalud.infrastructure.persistence.repositories;

import com.alidev.medisalud.infrastructure.persistence.entities.PenaltyJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface SpringDataPenaltyRepository extends JpaRepository<PenaltyJpaEntity, UUID> {
    List<PenaltyJpaEntity> findByPatientId(UUID patientId);
}