package com.alidev.medisalud.infrastructure.persistence.mappers;

import com.alidev.medisalud.domain.entities.Penalty;
import com.alidev.medisalud.infrastructure.persistence.entities.PenaltyJpaEntity;

public final class PenaltyMapper {
    private PenaltyMapper() {}

    public static Penalty toDomain(PenaltyJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        return Penalty.builder()
                .id(entity.getId())
                .patient(PatientMapper.toDomain(entity.getPatient()))
                .reservation(ReservationMapper.toDomain(entity.getReservation()))
                .reason(entity.getReason())
                .createdAt(entity.getCreatedAt())
                .expiresAt(entity.getExpiresAt())
                .build();
    }

    public static PenaltyJpaEntity toJpaEntity(Penalty penalty) {
        if (penalty == null) {
            return null;
        }
        return PenaltyJpaEntity.builder()
                .id(penalty.getId())
                .patient(PatientMapper.toJpaEntity(penalty.getPatient()))
                .reservation(ReservationMapper.toJpaEntity(penalty.getReservation()))
                .reason(penalty.getReason())
                .createdAt(penalty.getCreatedAt())
                .expiresAt(penalty.getExpiresAt())
                .build();
    }
}