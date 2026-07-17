package com.alidev.medisalud.domain.value_objects;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

public record DoctorSchedule(
        Map<DayOfWeek, ScheduleRange> schedules,
        int appointmentDurationMinutes
) {
    public static DoctorSchedule defaultSchedule() {
        return new DoctorSchedule(
                Map.of(
                        DayOfWeek.MONDAY,
                        new ScheduleRange(
                                LocalTime.of(8, 0),
                                LocalTime.of(18, 0)
                        ),

                        DayOfWeek.TUESDAY,
                        new ScheduleRange(
                                LocalTime.of(8, 0),
                                LocalTime.of(18, 0)
                        ),

                        DayOfWeek.WEDNESDAY,
                        new ScheduleRange(
                                LocalTime.of(8, 0),
                                LocalTime.of(18, 0)
                        ),

                        DayOfWeek.THURSDAY,
                        new ScheduleRange(
                                LocalTime.of(8, 0),
                                LocalTime.of(18, 0)
                        ),

                        DayOfWeek.FRIDAY,
                        new ScheduleRange(
                                LocalTime.of(8, 0),
                                LocalTime.of(18, 0)
                        ),

                        DayOfWeek.SATURDAY,
                        new ScheduleRange(
                                LocalTime.of(8, 0),
                                LocalTime.of(13, 0)
                        )
                ),
                30
        );
    }

    public record ScheduleRange(
            LocalTime startTime,
            LocalTime endTime
    ) {}

    public boolean isValidSlot(LocalDateTime dateTime) {
        ScheduleRange range = schedules.get(dateTime.getDayOfWeek());
        if (range == null) {
            return false;
        }
        LocalTime time = dateTime.toLocalTime();
        if (time.isBefore(range.startTime())
                || !time.isBefore(range.endTime())) {
            return false;
        }
        return time.getMinute() % appointmentDurationMinutes == 0;
    }
}