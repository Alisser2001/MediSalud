package com.alidev.medisalud.infrastructure.persistence.mappers;

import com.alidev.medisalud.domain.entities.Patient;
import com.alidev.medisalud.infrastructure.persistence.entities.PatientJpaEntity;

public final class PatientMapper {
    private PatientMapper() {}

    public static Patient toDomain(PatientJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        return Patient.builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .document(entity.getDocument())
                .birthDate(entity.getBirthDate())
                .phone(entity.getPhone())
                .email(entity.getEmail())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static PatientJpaEntity toJpaEntity(Patient patient) {
        if (patient == null) {
            return null;
        }
        return PatientJpaEntity.builder()
                .id(patient.getId())
                .fullName(patient.getFullName())
                .document(patient.getDocument())
                .birthDate(patient.getBirthDate())
                .phone(patient.getPhone())
                .email(patient.getEmail())
                .createdAt(patient.getCreatedAt())
                .updatedAt(patient.getUpdatedAt())
                .build();
    }
}