package com.alidev.medisalud.domain.ports.infrastructure.persistence;

import com.alidev.medisalud.domain.entities.Patient;
import java.util.Optional;
import java.util.UUID;

public interface PatientRepositoryPort {
    Patient save(Patient patient);
    Optional<Patient> findById(UUID patientId);
    Optional<Patient> findByDocument(String document);
}
