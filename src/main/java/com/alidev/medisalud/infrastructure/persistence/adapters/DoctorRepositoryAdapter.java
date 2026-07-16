package com.alidev.medisalud.infrastructure.persistence.adapters;

import com.alidev.medisalud.domain.entities.Doctor;
import com.alidev.medisalud.domain.ports.infrastructure.persistence.DoctorRepositoryPort;
import com.alidev.medisalud.infrastructure.persistence.mappers.DoctorMapper;
import com.alidev.medisalud.infrastructure.persistence.repositories.SpringDataDoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class DoctorRepositoryAdapter implements DoctorRepositoryPort {
    private final SpringDataDoctorRepository repository;

    @Override
    public Doctor save(Doctor doctor) {
        return DoctorMapper.toDomain(
                repository.save(
                        DoctorMapper.toJpaEntity(doctor)
                )
        );
    }

    @Override
    public Optional<Doctor> findById(UUID doctorId) {
        return repository.findById(doctorId).map(DoctorMapper::toDomain);
    }
}