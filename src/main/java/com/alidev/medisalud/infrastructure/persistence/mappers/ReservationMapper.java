package com.alidev.medisalud.infrastructure.persistence.mappers;

import com.alidev.medisalud.domain.entities.Reservation;
import com.alidev.medisalud.infrastructure.persistence.entities.ReservationJpaEntity;

public final class ReservationMapper {
    private ReservationMapper() {}

    public static Reservation toDomain(ReservationJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        return Reservation.builder()
                .id(entity.getId())
                .doctor(DoctorMapper.toDomain(entity.getDoctor()))
                .patient(PatientMapper.toDomain(entity.getPatient()))
                .scheduledAt(entity.getScheduledAt())
                .status(entity.getStatus())
                .cancelledAt(entity.getCancelledAt())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static ReservationJpaEntity toJpaEntity(Reservation reservation) {
        if (reservation == null) {
            return null;
        }
        return ReservationJpaEntity.builder()
                .id(reservation.getId())
                .doctor(DoctorMapper.toJpaEntity(reservation.getDoctor()))
                .patient(PatientMapper.toJpaEntity(reservation.getPatient()))
                .scheduledAt(reservation.getScheduledAt())
                .status(reservation.getStatus())
                .cancelledAt(reservation.getCancelledAt())
                .createdAt(reservation.getCreatedAt())
                .updatedAt(reservation.getUpdatedAt())
                .build();
    }
}