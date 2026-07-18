package com.alidev.medisalud.presentation.controllers;

import com.alidev.medisalud.application.dtos.request.GetReservationsRequest;
import com.alidev.medisalud.application.dtos.request.ScheduleReservationRequest;
import com.alidev.medisalud.application.dtos.response.CancelReservationResponse;
import com.alidev.medisalud.application.dtos.response.ReservationSummaryResponse;
import com.alidev.medisalud.application.dtos.response.ScheduleReservationResponse;
import com.alidev.medisalud.domain.ports.application.CancelReservationPort;
import com.alidev.medisalud.domain.ports.application.GetReservationsPort;
import com.alidev.medisalud.domain.ports.application.ScheduleReservationPort;
import com.alidev.medisalud.domain.value_objects.ReservationStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
@Tag(
        name = "Reservations",
        description = "Reservation management endpoints"
)
public class ReservationController {
    private final ScheduleReservationPort scheduleReservationPort;
    private final CancelReservationPort cancelReservationPort;
    private final GetReservationsPort getReservationsPort;

    @PostMapping
    @Operation(summary = "Schedule a reservation")
    public ResponseEntity<ScheduleReservationResponse> scheduleReservation(
            @Valid @RequestBody ScheduleReservationRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleReservationPort.execute(request));
    }

    @PatchMapping("/{reservationId}/cancel")
    @Operation(summary = "Cancel a reservation")
    public ResponseEntity<CancelReservationResponse> cancelReservation(
            @Parameter(description = "Reservation identifier")
            @PathVariable UUID reservationId
    ) {
        return ResponseEntity.ok(cancelReservationPort.execute(reservationId));
    }

    @GetMapping
    @Operation(summary = "Get reservations")
    public ResponseEntity<List<ReservationSummaryResponse>> getReservations(
            @RequestParam(required = false) UUID doctorId,
            @RequestParam(required = false) UUID patientId,
            @RequestParam(required = false) ReservationStatus status,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate
    ) {
        GetReservationsRequest request = new GetReservationsRequest(
                                                 doctorId,
                                                 patientId,
                                                 status,
                                                 startDate,
                                                 endDate
                                         );
        return ResponseEntity.ok(getReservationsPort.execute(request)
        );
    }
}