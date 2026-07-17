package com.alidev.medisalud.domain.ports.application;

import com.alidev.medisalud.application.dtos.response.GetDoctorAvailabilityResponse;
import java.time.LocalDate;
import java.util.UUID;

public interface GetDoctorAvailabilityPort {
    GetDoctorAvailabilityResponse execute(UUID doctorId, LocalDate startDate, LocalDate endDate);
}