package com.alidev.medisalud.infrastructure.persistence.adapters;

import com.alidev.medisalud.domain.entities.Penalty;
import com.alidev.medisalud.domain.ports.infrastructure.persistence.PenaltyRepositoryPort;
import com.alidev.medisalud.infrastructure.persistence.mappers.PenaltyMapper;
import com.alidev.medisalud.infrastructure.persistence.repositories.SpringDataPenaltyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PenaltyRepositoryAdapter implements PenaltyRepositoryPort {
    private final SpringDataPenaltyRepository repository;

    @Override
    public Penalty save(Penalty penalty) {
        return PenaltyMapper.toDomain(
                repository.save(
                        PenaltyMapper.toJpaEntity(penalty)
                )
        );
    }

    @Override
    public List<Penalty> findByPatientId(UUID patientId) {
        return repository.findByPatientId(patientId)
                .stream()
                .map(PenaltyMapper::toDomain)
                .toList();
    }
}