package com.alidev.medisalud.unit.domain.entities;

import com.alidev.medisalud.domain.entities.Reservation;
import com.alidev.medisalud.domain.exceptions.ReservationCancellationException;
import com.alidev.medisalud.domain.value_objects.ReservationStatus;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTest {
    @Test
    void shouldCancelReservationSuccessfully() {
        LocalDateTime now = LocalDateTime.now();
        Reservation reservation = Reservation.builder()
                .id(UUID.randomUUID())
                .status(ReservationStatus.SCHEDULED)
                .createdAt(now)
                .updatedAt(now)
                .build();
        LocalDateTime cancellationDate = now.plusMinutes(10);
        reservation.cancel(cancellationDate);
        assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.CANCELLED);
        assertThat(reservation.getCancelledAt()).isEqualTo(cancellationDate);
        assertThat(reservation.getUpdatedAt()).isEqualTo(cancellationDate);
    }

    @Test
    void shouldThrowExceptionWhenCancellingAlreadyCancelledReservation() {
        LocalDateTime now = LocalDateTime.now();
        Reservation reservation = Reservation.builder()
                .id(UUID.randomUUID())
                .status(ReservationStatus.CANCELLED)
                .cancelledAt(now)
                .createdAt(now)
                .updatedAt(now)
                .build();
        assertThatThrownBy(
                () -> reservation.cancel(LocalDateTime.now())
        )
                .isInstanceOf(ReservationCancellationException.class)
                .hasMessage("The reservation has already been cancelled.");
    }
}