package com.alidev.medisalud.infrastructure.persistence.adapters;

import com.alidev.medisalud.domain.entities.Penalty;
import com.alidev.medisalud.domain.ports.infrastructure.persistence.PenaltyRepositoryPort;
import com.alidev.medisalud.infrastructure.exceptions.RepositoryException;
import com.alidev.medisalud.infrastructure.persistence.mappers.PenaltyMapper;
import com.alidev.medisalud.infrastructure.persistence.repositories.SpringDataPenaltyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PenaltyRepositoryAdapter implements PenaltyRepositoryPort {
    private final SpringDataPenaltyRepository repository;

    @Override
    public Penalty save(Penalty penalty) {
        try{
            return PenaltyMapper.toDomain(
                    repository.save(
                            PenaltyMapper.toJpaEntity(penalty)
                    )
            );
        } catch (DataAccessException ex) {
            throw new RepositoryException("Error saving penalty.", ex);
        }
    }

    @Override
    public List<Penalty> findByPatientId(UUID patientId) {
        try{
            return repository.findByPatientId(patientId)
                    .stream()
                    .map(PenaltyMapper::toDomain)
                    .toList();
        } catch (DataAccessException ex) {
            throw new RepositoryException("Error retrieving penalties.", ex);
        }
    }
}