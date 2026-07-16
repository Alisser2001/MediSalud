package com.alidev.medisalud.application.use_cases;

import com.alidev.medisalud.application.dtos.request.RegisterPatientRequest;
import com.alidev.medisalud.application.dtos.response.RegisterPatientResponse;
import com.alidev.medisalud.application.mappers.PatientDtoMapper;
import com.alidev.medisalud.domain.entities.Patient;
import com.alidev.medisalud.domain.ports.application.RegisterPatientPort;
import com.alidev.medisalud.domain.ports.infrastructure.persistence.PatientRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterPatientUseCase implements RegisterPatientPort {
    private final PatientRepositoryPort patientRepository;

    @Override
    public RegisterPatientResponse execute(RegisterPatientRequest request) {
        Patient patient = PatientDtoMapper.fromRegisterRequest(request).initialize();
        Patient savedPatient = patientRepository.save(patient);
        return PatientDtoMapper.toRegisterResponse(savedPatient);
    }
}