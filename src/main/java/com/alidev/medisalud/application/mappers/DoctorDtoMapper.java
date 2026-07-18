package com.alidev.medisalud.application.mappers;

import com.alidev.medisalud.application.dtos.request.RegisterDoctorRequest;
import com.alidev.medisalud.application.dtos.response.RegisterDoctorResponse;
import com.alidev.medisalud.domain.entities.Doctor;

public final class DoctorDtoMapper {
    private DoctorDtoMapper() {}

    public static Doctor fromRegisterRequest(RegisterDoctorRequest request) {
        if (request == null) {
            return null;
        }
        return Doctor.builder()
                .fullName(request.fullName())
                .specialty(request.specialty())
                .phone(request.phone())
                .email(request.email())
                .build();
    }

    public static RegisterDoctorResponse toRegisterResponse(Doctor doctor) {
        if (doctor == null) {
            return null;
        }
        return new RegisterDoctorResponse(
                doctor.getId(),
                doctor.getFullName(),
                doctor.getSpecialty(),
                doctor.getPhone(),
                doctor.getEmail()
        );
    }
}