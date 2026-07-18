package com.alidev.medisalud.infrastructure.persistence.adapters;

import com.alidev.medisalud.domain.entities.Doctor;
import com.alidev.medisalud.domain.ports.infrastructure.persistence.DoctorRepositoryPort;
import com.alidev.medisalud.infrastructure.exceptions.RepositoryException;
import com.alidev.medisalud.infrastructure.persistence.mappers.DoctorMapper;
import com.alidev.medisalud.infrastructure.persistence.repositories.SpringDataDoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class DoctorRepositoryAdapter implements DoctorRepositoryPort {
    private final SpringDataDoctorRepository repository;

    @Override
    public Doctor save(Doctor doctor) {
        try{
            return DoctorMapper.toDomain(
                    repository.save(
                            DoctorMapper.toJpaEntity(doctor)
                    )
            );
        } catch (DataAccessException ex) {
            throw new RepositoryException("Error saving doctor.", ex);
        }
    }

    @Override
    public Optional<Doctor> findById(UUID doctorId) {
        try{
            return repository.findById(doctorId).map(DoctorMapper::toDomain);
        } catch (DataAccessException ex) {
            throw new RepositoryException("Error retrieving doctor.", ex);
        }
    }
}