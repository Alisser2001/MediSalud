package com.alidev.medisalud.infrastructure.persistence.adapters;

import com.alidev.medisalud.domain.entities.Patient;
import com.alidev.medisalud.domain.ports.infrastructure.persistence.PatientRepositoryPort;
import com.alidev.medisalud.infrastructure.exceptions.RepositoryException;
import com.alidev.medisalud.infrastructure.persistence.mappers.PatientMapper;
import com.alidev.medisalud.infrastructure.persistence.repositories.SpringDataPatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PatientRepositoryAdapter implements PatientRepositoryPort {
    private final SpringDataPatientRepository repository;

    @Override
    public Patient save(Patient patient) {
        try{
            return PatientMapper.toDomain(
                    repository.save(
                            PatientMapper.toJpaEntity(patient)
                    )
            );
        } catch (DataAccessException ex) {
            throw new RepositoryException("Error saving patient.", ex);
        }
    }

    @Override
    public Optional<Patient> findById(UUID patientId) {
        try{
            return repository.findById(patientId).map(PatientMapper::toDomain);
        } catch (DataAccessException ex) {
            throw new RepositoryException("Error retrieving patient.", ex);
        }
    }

    @Override
    public Optional<Patient> findByDocument(String document) {
        try{
            return repository.findByDocument(document).map(PatientMapper::toDomain);
        } catch (DataAccessException ex) {
            throw new RepositoryException("Error retrieving patient by document.", ex);
        }
    }
}