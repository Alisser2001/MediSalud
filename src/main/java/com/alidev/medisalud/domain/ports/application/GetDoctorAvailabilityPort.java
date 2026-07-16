package com.alidev.medisalud.domain.ports.application;

import com.alidev.medisalud.application.dtos.request.GetDoctorAvailabilityRequest;
import com.alidev.medisalud.application.dtos.response.GetDoctorAvailabilityResponse;

public interface GetDoctorAvailabilityPort {
    GetDoctorAvailabilityResponse execute(GetDoctorAvailabilityRequest request);
}