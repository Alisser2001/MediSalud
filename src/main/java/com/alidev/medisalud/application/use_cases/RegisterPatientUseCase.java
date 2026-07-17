package com.alidev.medisalud.application.use_cases;

import com.alidev.medisalud.application.dtos.request.RegisterPatientRequest;
import com.alidev.medisalud.application.dtos.response.RegisterPatientResponse;
import com.alidev.medisalud.application.mappers.PatientDtoMapper;
import com.alidev.medisalud.domain.entities.Patient;
import com.alidev.medisalud.domain.exceptions.DuplicatePatientDocumentException;
import com.alidev.medisalud.domain.exceptions.InvalidPatientBirthDateException;
import com.alidev.medisalud.domain.ports.application.RegisterPatientPort;
import com.alidev.medisalud.domain.ports.infrastructure.persistence.PatientRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class RegisterPatientUseCase implements RegisterPatientPort {
    private final PatientRepositoryPort patientRepository;

    @Override
    public RegisterPatientResponse execute(RegisterPatientRequest request) {
        patientRepository
                .findByDocument(request.document())
                .ifPresent(
                patient -> {
                    throw new DuplicatePatientDocumentException(
                            "A patient with document '%s' already exists.".formatted(request.document())
                    );
                });
        if (request.birthDate().isAfter(LocalDate.now())) {
            throw new InvalidPatientBirthDateException(
                    "Birth date cannot be in the future."
            );
        }
        Patient patient = PatientDtoMapper.fromRegisterRequest(request).initialize();
        Patient savedPatient = patientRepository.save(patient);
        return PatientDtoMapper.toRegisterResponse(savedPatient);
    }
}