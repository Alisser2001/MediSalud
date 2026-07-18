package com.alidev.medisalud.domain.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@ToString
public class Patient {
    private final UUID id;
    private final String fullName;
    private final String document;
    private final LocalDate birthDate;
    private final String phone;
    private final String email;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public Patient initialize() {
        LocalDateTime now = LocalDateTime.now();
        return Patient.builder()
                .id(UUID.randomUUID())
                .fullName(this.fullName)
                .document(this.document)
                .birthDate(this.birthDate)
                .phone(this.phone)
                .email(this.email)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }
}