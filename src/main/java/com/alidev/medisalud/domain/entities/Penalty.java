package com.alidev.medisalud.domain.entities;

import com.alidev.medisalud.domain.value_objects.PenaltyReason;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@ToString
public class Penalty {
    private final UUID id;
    private final Patient patient;
    private final Reservation reservation;
    private final PenaltyReason reason;
    private final LocalDateTime createdAt;
    private final LocalDateTime expiresAt;

    public Penalty initialize() {
        return Penalty.builder()
                .id(UUID.randomUUID())
                .patient(this.patient)
                .reservation(this.reservation)
                .reason(this.reason)
                .createdAt(LocalDateTime.now())
                .expiresAt(this.expiresAt)
                .build();
    }
}