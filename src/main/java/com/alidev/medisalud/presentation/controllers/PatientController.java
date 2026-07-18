package com.alidev.medisalud.presentation.controllers;

import com.alidev.medisalud.application.dtos.request.RegisterPatientRequest;
import com.alidev.medisalud.application.dtos.response.RegisterPatientResponse;
import com.alidev.medisalud.domain.ports.application.RegisterPatientPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
@Tag(
        name = "Patients",
        description = "Patient management endpoints"
)
public class PatientController {
    private final RegisterPatientPort registerPatientPort;

    @PostMapping
    @Operation(summary = "Register a patient")
    public ResponseEntity<RegisterPatientResponse> registerPatient(
            @Valid @RequestBody RegisterPatientRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(registerPatientPort.execute(request));
    }
}