package com.alidev.medisalud.infrastructure.persistence.repositories;

import com.alidev.medisalud.infrastructure.persistence.entities.DoctorJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SpringDataDoctorRepository extends JpaRepository<DoctorJpaEntity, UUID> {}