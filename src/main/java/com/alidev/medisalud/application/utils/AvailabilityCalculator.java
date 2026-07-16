package com.alidev.medisalud.application.utils;

import com.alidev.medisalud.application.dtos.response.AvailableSlotResponse;
import com.alidev.medisalud.domain.entities.Reservation;
import com.alidev.medisalud.domain.value_objects.DoctorSchedule;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public final class AvailabilityCalculator {
    private AvailabilityCalculator() {}

    public static List<AvailableSlotResponse> calculate(
            DoctorSchedule schedule,
            LocalDate startDate,
            LocalDate endDate,
            List<Reservation> reservations
    ) {
        List<AvailableSlotResponse> availableSlots = new java.util.ArrayList<>();
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
            DoctorSchedule.ScheduleRange range = schedule.schedules().get(dayOfWeek);
            if (range != null) {
                LocalTime currentTime = range.startTime();
                while (currentTime.isBefore(range.endTime())) {
                    LocalDateTime slotDateTime = LocalDateTime.of(currentDate, currentTime);
                    boolean reserved = reservations
                                        .stream()
                                        .anyMatch(reservation -> reservation.getScheduledAt().equals(slotDateTime));
                    if (!reserved) {
                        availableSlots.add(
                                new AvailableSlotResponse(
                                        slotDateTime,
                                        slotDateTime.plusMinutes(
                                                schedule.appointmentDurationMinutes()
                                        )
                                )
                        );
                    }
                    currentTime = currentTime.plusMinutes(schedule.appointmentDurationMinutes());
                }
            }
            currentDate = currentDate.plusDays(1);
        }
        return availableSlots;
    }
}