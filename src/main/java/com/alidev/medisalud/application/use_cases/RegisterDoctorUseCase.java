package com.alidev.medisalud.application.use_cases;

import com.alidev.medisalud.application.dtos.request.RegisterDoctorRequest;
import com.alidev.medisalud.application.dtos.response.RegisterDoctorResponse;
import com.alidev.medisalud.application.mappers.DoctorDtoMapper;
import com.alidev.medisalud.domain.entities.Doctor;
import com.alidev.medisalud.domain.ports.application.RegisterDoctorPort;
import com.alidev.medisalud.domain.ports.infrastructure.persistence.DoctorRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterDoctorUseCase implements RegisterDoctorPort {
    private final DoctorRepositoryPort doctorRepository;

    @Override
    public RegisterDoctorResponse execute(RegisterDoctorRequest request) {
        Doctor doctor = DoctorDtoMapper.fromRegisterRequest(request).initialize();
        Doctor savedDoctor = doctorRepository.save(doctor);
        return DoctorDtoMapper.toRegisterResponse(savedDoctor);
    }
}