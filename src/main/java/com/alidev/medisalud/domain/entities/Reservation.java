package com.alidev.medisalud.domain.entities;

import com.alidev.medisalud.domain.exceptions.ReservationCancellationException;
import com.alidev.medisalud.domain.value_objects.ReservationStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@ToString
public class Reservation {
    private final UUID id;
    private final Doctor doctor;
    private final Patient patient;
    private final LocalDateTime scheduledAt;
    private ReservationStatus status;
    private LocalDateTime cancelledAt;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void cancel(LocalDateTime cancelledAt) {
        if (isCancelled()) {
            throw new ReservationCancellationException(
                    "The reservation has already been cancelled."
            );
        }
        this.status = ReservationStatus.CANCELLED;
        this.cancelledAt = cancelledAt;
        this.updatedAt = cancelledAt;
    }

    public boolean isCancelled() {
        return ReservationStatus.CANCELLED.equals(this.status);
    }

    public Reservation initialize() {
        LocalDateTime now = LocalDateTime.now();
        return Reservation.builder()
                .id(UUID.randomUUID())
                .doctor(this.doctor)
                .patient(this.patient)
                .scheduledAt(this.scheduledAt)
                .status(ReservationStatus.SCHEDULED)
                .cancelledAt(null)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }
}
