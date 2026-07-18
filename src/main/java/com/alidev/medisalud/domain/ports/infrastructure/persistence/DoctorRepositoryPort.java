package com.alidev.medisalud.domain.ports.infrastructure.persistence;

import com.alidev.medisalud.domain.entities.Doctor;
import java.util.Optional;
import java.util.UUID;

public interface DoctorRepositoryPort {
    Doctor save(Doctor doctor);
    Optional<Doctor> findById(UUID doctorId);
}
