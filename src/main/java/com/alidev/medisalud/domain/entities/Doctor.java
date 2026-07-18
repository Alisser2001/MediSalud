package com.alidev.medisalud.domain.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@ToString
public class Doctor {
    private final UUID id;
    private final String fullName;
    private final String specialty;
    private final String phone;
    private final String email;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public Doctor initialize() {
        LocalDateTime now = LocalDateTime.now();
        return Doctor.builder()
                .id(UUID.randomUUID())
                .fullName(this.fullName)
                .specialty(this.specialty)
                .phone(this.phone)
                .email(this.email)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }
}