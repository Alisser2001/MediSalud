package com.alidev.medisalud.application.dtos.response;

import java.time.LocalDateTime;

public record AvailableSlotResponse(
        LocalDateTime startAt,
        LocalDateTime endAt
) {}
