package com.alidev.medisalud.presentation.controllers;

import com.alidev.medisalud.application.dtos.request.RegisterDoctorRequest;
import com.alidev.medisalud.application.dtos.response.GetDoctorAvailabilityResponse;
import com.alidev.medisalud.application.dtos.response.RegisterDoctorResponse;
import com.alidev.medisalud.domain.ports.application.GetDoctorAvailabilityPort;
import com.alidev.medisalud.domain.ports.application.RegisterDoctorPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final RegisterDoctorPort registerDoctorPort;
    private final GetDoctorAvailabilityPort getDoctorAvailabilityPort;

    @PostMapping
    public ResponseEntity<RegisterDoctorResponse> registerDoctor(@RequestBody RegisterDoctorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(registerDoctorPort.execute(request));
    }

    @GetMapping("/{doctorId}/availability")
    public ResponseEntity<GetDoctorAvailabilityResponse> getAvailability(
            @PathVariable UUID doctorId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        return ResponseEntity.ok(
                getDoctorAvailabilityPort.execute(
                        doctorId,
                        startDate,
                        endDate
                )
        );
    }
}