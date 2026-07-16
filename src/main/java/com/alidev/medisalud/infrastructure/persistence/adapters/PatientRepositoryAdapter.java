package com.alidev.medisalud.infrastructure.persistence.adapters;

import com.alidev.medisalud.domain.entities.Patient;
import com.alidev.medisalud.domain.ports.infrastructure.persistence.PatientRepositoryPort;
import com.alidev.medisalud.infrastructure.persistence.mappers.PatientMapper;
import com.alidev.medisalud.infrastructure.persistence.repositories.SpringDataPatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PatientRepositoryAdapter implements PatientRepositoryPort {
    private final SpringDataPatientRepository repository;

    @Override
    public Patient save(Patient patient) {
        return PatientMapper.toDomain(
                repository.save(
                        PatientMapper.toJpaEntity(patient)
                )
        );
    }

    @Override
    public Optional<Patient> findById(UUID patientId) {
        return repository.findById(patientId).map(PatientMapper::toDomain);
    }

    @Override
    public Optional<Patient> findByDocument(String document) {
        return repository.findByDocument(document).map(PatientMapper::toDomain);
    }
}