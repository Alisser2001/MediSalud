package com.alidev.medisalud.presentation.controllers;

import com.alidev.medisalud.application.dtos.request.RegisterPatientRequest;
import com.alidev.medisalud.application.dtos.response.RegisterPatientResponse;
import com.alidev.medisalud.domain.ports.application.RegisterPatientPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
public class PatientController {
    private final RegisterPatientPort registerPatientPort;

    @PostMapping
    public ResponseEntity<RegisterPatientResponse> registerPatient(@RequestBody RegisterPatientRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(registerPatientPort.execute(request));
    }
}