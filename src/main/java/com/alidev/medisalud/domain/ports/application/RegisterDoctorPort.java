package com.alidev.medisalud.domain.ports.application;

import com.alidev.medisalud.application.dtos.request.RegisterDoctorRequest;
import com.alidev.medisalud.application.dtos.response.RegisterDoctorResponse;

public interface RegisterDoctorPort {
    RegisterDoctorResponse execute(RegisterDoctorRequest request);
}