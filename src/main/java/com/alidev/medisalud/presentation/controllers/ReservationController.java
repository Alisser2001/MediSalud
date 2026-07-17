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
public class ReservationController {
    private final ScheduleReservationPort scheduleReservationPort;
    private final CancelReservationPort cancelReservationPort;
    private final GetReservationsPort getReservationsPort;

    @PostMapping
    public ResponseEntity<ScheduleReservationResponse> scheduleReservation(@RequestBody ScheduleReservationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleReservationPort.execute(request));
    }

    @PatchMapping("/{reservationId}/cancel")
    public ResponseEntity<CancelReservationResponse> cancelReservation(@PathVariable UUID reservationId) {
        return ResponseEntity.ok(cancelReservationPort.execute(reservationId));
    }

    @GetMapping
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