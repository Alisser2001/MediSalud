package com.alidev.medisalud.infrastructure.persistence.repositories;

import com.alidev.medisalud.infrastructure.persistence.entities.PatientJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataPatientRepository extends JpaRepository<PatientJpaEntity, UUID> {
    Optional<PatientJpaEntity> findByDocument(String document);
}