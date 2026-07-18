package com.alidev.medisalud.infrastructure.persistence.mappers;

import com.alidev.medisalud.domain.entities.Doctor;
import com.alidev.medisalud.infrastructure.persistence.entities.DoctorJpaEntity;

public final class DoctorMapper {
    private DoctorMapper() {}

    public static Doctor toDomain(DoctorJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        return Doctor.builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .specialty(entity.getSpecialty())
                .phone(entity.getPhone())
                .email(entity.getEmail())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static DoctorJpaEntity toJpaEntity(Doctor doctor) {
        if (doctor == null) {
            return null;
        }
        return DoctorJpaEntity.builder()
                .id(doctor.getId())
                .fullName(doctor.getFullName())
                .specialty(doctor.getSpecialty())
                .phone(doctor.getPhone())
                .email(doctor.getEmail())
                .createdAt(doctor.getCreatedAt())
                .updatedAt(doctor.getUpdatedAt())
                .build();
    }
}