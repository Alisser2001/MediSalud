package com.alidev.medisalud.unit.application.utils;

import com.alidev.medisalud.application.dtos.response.AvailableSlotResponse;
import com.alidev.medisalud.application.utils.AvailabilityCalculator;
import com.alidev.medisalud.domain.entities.Reservation;
import com.alidev.medisalud.domain.value_objects.DoctorSchedule;
import com.alidev.medisalud.domain.value_objects.ReservationStatus;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

class AvailabilityCalculatorTest {
    private final DoctorSchedule schedule = DoctorSchedule.defaultSchedule();

    @Test
    void shouldGenerateAllAvailableSlotsWhenNoReservationsExist() {
        LocalDate date = LocalDate.of(2026, 7, 20);
        List<AvailableSlotResponse> slots =
                AvailabilityCalculator.calculate(
                        schedule,
                        date,
                        date,
                        List.of()
                );
        assertThat(slots).hasSize(20);
    }

    @Test
    void shouldExcludeReservedSlotsFromAvailability() {
        LocalDate date = LocalDate.of(2026, 7, 20);
        Reservation reservation = buildReservation(LocalDateTime.of(2026, 7, 20, 10, 0));
        List<AvailableSlotResponse> slots =
                AvailabilityCalculator.calculate(
                        schedule,
                        date,
                        date,
                        List.of(reservation)
                );
        assertThat(slots).hasSize(19);
        assertThat(
                slots.stream()
                        .noneMatch(slot ->
                                slot.startAt().equals(
                                        LocalDateTime.of(2026, 7, 20, 10, 0)
                                )
                        )
        ).isTrue();
    }

    @Test
    void shouldGenerateWeekdayAvailability() {
        LocalDate date = LocalDate.of(2026, 7, 20);
        List<AvailableSlotResponse> slots =
                AvailabilityCalculator.calculate(
                        schedule,
                        date,
                        date,
                        List.of()
                );
        assertThat(slots).hasSize(20);
        assertThat(slots.getFirst().startAt()).isEqualTo(LocalDateTime.of(2026, 7, 20, 8, 0));
        assertThat(slots.getLast().startAt()).isEqualTo(LocalDateTime.of(2026, 7, 20, 17, 30));
    }

    @Test
    void shouldGenerateSaturdayAvailability() {
        LocalDate date = LocalDate.of(2026, 7, 18);
        List<AvailableSlotResponse> slots =
                AvailabilityCalculator.calculate(
                        schedule,
                        date,
                        date,
                        List.of()
                );
        assertThat(slots).hasSize(10);
        assertThat(slots.getFirst().startAt()).isEqualTo(LocalDateTime.of(2026, 7, 18, 8, 0));
        assertThat(slots.getLast().startAt()).isEqualTo(LocalDateTime.of(2026, 7, 18, 12, 30));
    }

    @Test
    void shouldGenerateAvailabilityAcrossDateRange() {
        LocalDate startDate = LocalDate.of(2026, 7, 20);
        LocalDate endDate = LocalDate.of(2026, 7, 21);
        List<AvailableSlotResponse> slots =
                AvailabilityCalculator.calculate(
                        schedule,
                        startDate,
                        endDate,
                        List.of()
                );
        assertThat(slots).hasSize(40);
    }

    @Test
    void shouldReturnEmptyAvailabilityWhenRangeContainsOnlyNonWorkingDays() {
        LocalDate sunday = LocalDate.of(2026, 7, 19);
        List<AvailableSlotResponse> slots =
                AvailabilityCalculator.calculate(
                        schedule,
                        sunday,
                        sunday,
                        List.of()
                );
        assertThat(slots).isEmpty();
    }

    private Reservation buildReservation(LocalDateTime scheduledAt) {
        return Reservation.builder()
                .id(UUID.randomUUID())
                .scheduledAt(scheduledAt)
                .status(ReservationStatus.SCHEDULED)
                .build();
    }
}