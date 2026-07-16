package com.alidev.medisalud.domain.ports.application;

import com.alidev.medisalud.application.dtos.request.RegisterPatientRequest;
import com.alidev.medisalud.application.dtos.response.RegisterPatientResponse;

public interface RegisterPatientPort {
    RegisterPatientResponse execute(RegisterPatientRequest request);
}