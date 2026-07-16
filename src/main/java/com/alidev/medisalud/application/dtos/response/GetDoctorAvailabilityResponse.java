package com.alidev.medisalud.application.dtos.response;

import java.util.List;
import java.util.UUID;

public record GetDoctorAvailabilityResponse(
        UUID doctorId,
        List<AvailableSlotResponse> availableSlots
) {}