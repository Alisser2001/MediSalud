package com.alidev.medisalud.infrastructure.persistence.entities;

import com.alidev.medisalud.domain.value_objects.PenaltyReason;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "penalty")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PenaltyJpaEntity {
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientJpaEntity patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private ReservationJpaEntity reservation;

    @Column(nullable = false)
    private PenaltyReason reason;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;
}