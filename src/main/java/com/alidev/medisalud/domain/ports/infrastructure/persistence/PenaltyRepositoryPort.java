package com.alidev.medisalud.domain.ports.infrastructure.persistence;

import com.alidev.medisalud.domain.entities.Penalty;
import java.util.List;
import java.util.UUID;

public interface PenaltyRepositoryPort {
    Penalty save(Penalty penalty);
    List<Penalty> findByPatientId(UUID patientId);
}