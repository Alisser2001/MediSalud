package com.alidev.medisalud.application.mappers;

import com.alidev.medisalud.application.dtos.request.RegisterPatientRequest;
import com.alidev.medisalud.application.dtos.response.RegisterPatientResponse;
import com.alidev.medisalud.domain.entities.Patient;

public final class PatientDtoMapper {
    private PatientDtoMapper() {}

    public static Patient fromRegisterRequest(RegisterPatientRequest request) {
        if (request == null) {
            return null;
        }
        return Patient.builder()
                .fullName(request.fullName())
                .document(request.document())
                .birthDate(request.birthDate())
                .phone(request.phone())
                .email(request.email())
                .build();
    }

    public static RegisterPatientResponse toRegisterResponse(Patient patient) {
        if (patient == null) {
            return null;
        }
        return new RegisterPatientResponse(
                patient.getId(),
                patient.getFullName(),
                patient.getDocument(),
                patient.getPhone(),
                patient.getEmail()
        );
    }
}