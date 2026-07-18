package com.alidev.medisalud.unit.application.use_cases;

import com.alidev.medisalud.application.dtos.request.RegisterDoctorRequest;
import com.alidev.medisalud.application.dtos.response.RegisterDoctorResponse;
import com.alidev.medisalud.application.use_cases.RegisterDoctorUseCase;
import com.alidev.medisalud.domain.entities.Doctor;
import com.alidev.medisalud.domain.ports.infrastructure.persistence.DoctorRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterDoctorUseCaseTest {
    @Mock
    private DoctorRepositoryPort doctorRepository;

    @InjectMocks
    private RegisterDoctorUseCase useCase;

    @Test
    void shouldRegisterDoctorSuccessfully() {
        RegisterDoctorRequest request =
                new RegisterDoctorRequest(
                        "Dr. John Doe",
                        "Cardiology",
                        "1234567",
                        "doctor@test.com"
                );
        Doctor savedDoctor =
                Doctor.builder()
                        .id(UUID.randomUUID())
                        .fullName("Dr. John Doe")
                        .specialty("Cardiology")
                        .phone("1234567")
                        .email("doctor@test.com")
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();
        when(doctorRepository.save(any(Doctor.class))).thenReturn(savedDoctor);
        RegisterDoctorResponse response = useCase.execute(request);
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(savedDoctor.getId());
        assertThat(response.fullName()).isEqualTo(savedDoctor.getFullName());
        assertThat(response.specialty()).isEqualTo(savedDoctor.getSpecialty());
        assertThat(response.phone()).isEqualTo(savedDoctor.getPhone());
        assertThat(response.email()).isEqualTo(savedDoctor.getEmail());
    }
}