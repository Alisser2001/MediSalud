package com.alidev.medisalud.unit.domain.value_objects;

import com.alidev.medisalud.domain.value_objects.DoctorSchedule;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

class DoctorScheduleTest {
    private final DoctorSchedule schedule = DoctorSchedule.defaultSchedule();

    @Test
    void shouldAcceptWeekdaySlotAtOpeningTime() {
        LocalDateTime dateTime = LocalDateTime.of(2026, 7, 20, 8, 0);
        boolean result = schedule.isValidSlot(dateTime);
        assertThat(result).isTrue();
    }

    @Test
    void shouldAcceptWeekdaySlotAtClosingBoundary() {
        LocalDateTime dateTime = LocalDateTime.of(2026, 7, 20, 17, 30);
        boolean result = schedule.isValidSlot(dateTime);
        assertThat(result).isTrue();
    }

    @Test
    void shouldAcceptSaturdaySlot() {
        LocalDateTime dateTime = LocalDateTime.of(2026, 7, 18, 12, 30);
        boolean result = schedule.isValidSlot(dateTime);
        assertThat(result).isTrue();
    }

    @Test
    void shouldAcceptValidThirtyMinuteSlot() {
        LocalDateTime dateTime = LocalDateTime.of(2026, 7, 20, 10, 30);
        boolean result = schedule.isValidSlot(dateTime);
        assertThat(result).isTrue();
    }

    @Test
    void shouldRejectSundaySlot() {
        LocalDateTime dateTime = LocalDateTime.of(2026, 7, 19, 10, 0);
        boolean result = schedule.isValidSlot(dateTime);
        assertThat(result).isFalse();
    }

    @Test
    void shouldRejectWeekdaySlotBeforeOpeningHours() {
        LocalDateTime dateTime = LocalDateTime.of(2026, 7, 20, 7, 30);
        boolean result = schedule.isValidSlot(dateTime);
        assertThat(result).isFalse();
    }

    @Test
    void shouldRejectWeekdaySlotAfterClosingHours() {
        LocalDateTime dateTime = LocalDateTime.of(2026, 7, 20, 18, 0);
        boolean result = schedule.isValidSlot(dateTime);
        assertThat(result).isFalse();
    }

    @Test
    void shouldRejectSaturdaySlotAfterClosingHours() {
        LocalDateTime dateTime = LocalDateTime.of(2026, 7, 18, 13, 0);
        boolean result = schedule.isValidSlot(dateTime);
        assertThat(result).isFalse();
    }

    @Test
    void shouldRejectFifteenMinuteSlot() {
        LocalDateTime dateTime = LocalDateTime.of(2026, 7, 20, 8, 15);
        boolean result = schedule.isValidSlot(dateTime);
        assertThat(result).isFalse();
    }

    @Test
    void shouldRejectNonThirtyMinuteSlot() {
        LocalDateTime dateTime = LocalDateTime.of(2026, 7, 20, 9, 17);
        boolean result = schedule.isValidSlot(dateTime);
        assertThat(result).isFalse();
    }
}