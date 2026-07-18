package com.alidev.medisalud.unit.application.use_cases;

import com.alidev.medisalud.application.dtos.request.RegisterPatientRequest;
import com.alidev.medisalud.application.dtos.response.RegisterPatientResponse;
import com.alidev.medisalud.application.use_cases.RegisterPatientUseCase;
import com.alidev.medisalud.domain.entities.Patient;
import com.alidev.medisalud.domain.exceptions.DuplicatePatientDocumentException;
import com.alidev.medisalud.domain.ports.infrastructure.persistence.PatientRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterPatientUseCaseTest {
    @Mock
    private PatientRepositoryPort patientRepository;

    @InjectMocks
    private RegisterPatientUseCase useCase;

    @Test
    void shouldRegisterPatientSuccessfully() {
        RegisterPatientRequest request =
                new RegisterPatientRequest(
                        "John Doe",
                        "1234567",
                        null,
                        "1234567",
                        "john@test.com"
                );
        Patient savedPatient = buildPatient(null);
        when(patientRepository.findByDocument(request.document())).thenReturn(Optional.empty());
        when(patientRepository.save(any(Patient.class))).thenReturn(savedPatient);
        RegisterPatientResponse response = useCase.execute(request);
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(savedPatient.getId());
        assertThat(response.document()).isEqualTo(savedPatient.getDocument());
        assertThat(response.fullName()).isEqualTo(savedPatient.getFullName());
    }

    @Test
    void shouldRegisterPatientWithBirthDate() {
        LocalDate birthDate = LocalDate.of(1995, 5, 10);
        RegisterPatientRequest request =
                new RegisterPatientRequest(
                        "John Doe",
                        "1234567",
                        birthDate,
                        "1234567",
                        "john@test.com"
                );
        Patient savedPatient = buildPatient(birthDate);
        when(patientRepository.findByDocument(request.document())).thenReturn(Optional.empty());
        when(patientRepository.save(any(Patient.class))).thenReturn(savedPatient);
        RegisterPatientResponse response = useCase.execute(request);
        assertThat(response).isNotNull();
        assertThat(response.document()).isEqualTo("1234567");
    }

    @Test
    void shouldRegisterPatientWithoutBirthDate() {
        RegisterPatientRequest request =
                new RegisterPatientRequest(
                        "John Doe",
                        "1234567",
                        null,
                        "1234567",
                        "john@test.com"
                );
        Patient savedPatient = buildPatient(null);
        when(patientRepository.findByDocument(request.document())).thenReturn(Optional.empty());
        when(patientRepository.save(any(Patient.class))).thenReturn(savedPatient);
        RegisterPatientResponse response = useCase.execute(request);
        assertThat(response).isNotNull();
        assertThat(response.document()).isEqualTo("1234567");
    }

    @Test
    void shouldThrowExceptionWhenDocumentAlreadyExists() {
        RegisterPatientRequest request =
                new RegisterPatientRequest(
                        "John Doe",
                        "1234567",
                        null,
                        "1234567",
                        "john@test.com"
                );
        Patient existingPatient = buildPatient(null);
        when(patientRepository.findByDocument(request.document())).thenReturn(Optional.of(existingPatient));
        assertThatThrownBy(
                () -> useCase.execute(request)
        ).isInstanceOf(DuplicatePatientDocumentException.class).hasMessage("A patient with document '1234567' already exists.");
    }

    private Patient buildPatient(LocalDate birthDate) {
        return Patient.builder()
                .id(UUID.randomUUID())
                .fullName("John Doe")
                .document("1234567")
                .birthDate(birthDate)
                .phone("1234567")
                .email("john@test.com")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}