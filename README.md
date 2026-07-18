# MediSalud API

Sistema de agendamiento de citas médicas desarrollado como solución para la prueba técnica de backend Java.

## Tecnologías Utilizadas

- Java 21
- Spring Boot 4.1
- Spring Data JPA
- PostgreSQL
- Flyway
- Spring Validation
- SpringDoc OpenAPI (Swagger)
- Lombok
- JUnit 5
- Mockito
- Maven

---

## Arquitectura Elegida

Se implementó una Arquitectura Hexagonal (Ports & Adapters), cuyo objetivo principal es desacoplar la lógica de negocio de los detalles técnicos como la base de datos, el framework web o cualquier integración externa.

La aplicación fue diseñada siguiendo el principio de inversión de dependencias, permitiendo que el dominio y los casos de uso permanezcan independientes de Spring Boot, JPA o PostgreSQL.

### Estructura General

```text
presentation
    ↓
application
    ↓
domain
    ↑
infrastructure
```

### Capa de Presentación

Responsable de exponer la API REST.

Contiene:

- Controllers
- Validación de requests mediante Bean Validation
- Documentación OpenAPI / Swagger

Su responsabilidad es recibir solicitudes HTTP, transformarlas en DTOs de aplicación y delegar la ejecución a los casos de uso.

Ejemplo:

```text
POST /api/v1/reservations
        ↓
ReservationController
        ↓
ScheduleReservationUseCase
```

---

### Capa de Aplicación

Implementa los casos de uso del sistema.

Contiene:

- Use Cases
- DTOs de entrada y salida
- Mappers
- Utilidades de aplicación

Los casos de uso coordinan el flujo de ejecución y aplican las reglas de negocio que involucran múltiples entidades o repositorios.

Ejemplos:

- Registrar paciente
- Registrar médico
- Agendar cita
- Cancelar cita
- Consultar disponibilidad
- Consultar reservas

La capa de aplicación depende únicamente de puertos definidos en el dominio.

---

### Capa de Dominio

Es el núcleo de la aplicación.

Contiene:

- Entidades
- Value Objects
- Excepciones de negocio
- Puertos (Interfaces)

Aquí se encuentran las reglas de negocio más importantes:

- Horarios válidos de atención
- Validación de franjas de 30 minutos
- Penalizaciones por cancelación
- Restricciones de agendamiento
- Estados de las citas

La capa de dominio no depende de Spring Boot, JPA ni ninguna tecnología externa.

Esto permite que la lógica de negocio pueda probarse de forma aislada mediante pruebas unitarias.

---

### Capa de Infraestructura

Implementa los contratos definidos por el dominio.

Contiene:

- Repositorios JPA
- Entidades de persistencia
- Adaptadores de repositorio
- Configuración técnica

Su responsabilidad es traducir las operaciones del dominio hacia PostgreSQL utilizando Spring Data JPA.

Ejemplo:

```text
PatientRepositoryPort
        ↑
PatientRepositoryAdapter
        ↓
JpaPatientRepository
        ↓
PostgreSQL
```

---

## Manejo de Errores

La aplicación implementa una estrategia centralizada de manejo de errores basada en excepciones de dominio, excepciones de infraestructura y un manejador global de excepciones.

### Objetivos

- Mantener desacoplada la lógica de negocio de HTTP.
- Evitar bloques try/catch distribuidos por toda la aplicación.
- Retornar respuestas consistentes al consumidor de la API.
- Proporcionar mensajes de error claros y accionables.

---

### Excepciones de Dominio

Representan violaciones de reglas de negocio definidas por el sistema.

Estas excepciones son lanzadas desde las entidades o casos de uso cuando una operación no puede completarse debido a una restricción funcional.

Ejemplos:

- ResourceNotFoundException
- DuplicatePatientDocumentException
- InvalidReservationScheduleException
- PatientBlockedException
- ReservationAlreadyCancelledException

Ejemplo:

```text
Paciente con 3 penalizaciones activas
        ↓
ScheduleReservationUseCase
        ↓
PatientBlockedException
```

---

### Excepciones de Infraestructura

Representan errores técnicos relacionados con dependencias externas.

Estas excepciones son lanzadas por los adapters de infraestructura cuando ocurre un problema durante la comunicación con componentes externos.

Ejemplos:

- RepositoryException
- DatabaseConnectionException
- ExternalServiceException
- AuthenticationException

Ejemplo:

```text
PostgreSQL no disponible
        ↓
PatientRepositoryAdapter
        ↓
DatabaseConnectionException
```

De esta forma la capa de aplicación no necesita conocer detalles técnicos de persistencia.

---

### GlobalExceptionHandler

Todas las excepciones son interceptadas por un único componente global basado en @RestControllerAdvice.

Su responsabilidad es traducir excepciones internas a respuestas HTTP consistentes.

Ejemplo:

```text
DuplicatePatientDocumentException
        ↓
GlobalExceptionHandler
        ↓
HTTP 409 CONFLICT
```

---

### Mapeo de Errores HTTP

| Excepción | HTTP |
|------------|------|
| MethodArgumentNotValidException | 400 Bad Request |
| ResourceNotFoundException | 404 Not Found |
| DuplicatePatientDocumentException | 409 Conflict |
| InvalidReservationScheduleException | 409 Conflict |
| PatientBlockedException | 409 Conflict |
| ReservationAlreadyCancelledException | 409 Conflict |
| RepositoryException | 500 Internal Server Error |
| DatabaseConnectionException | 500 Internal Server Error |
| Exception | 500 Internal Server Error |

---

### Formato de Respuesta de Error

Todas las respuestas de error utilizan una estructura uniforme.

Ejemplo:

```json
{
  "timestamp": "2026-07-18T15:30:00",
  "status": 409,
  "error": "Conflict",
  "message": "Patient has 3 active penalties."
}
```

Esto permite que clientes y sistemas consumidores puedan procesar errores de forma consistente independientemente de la operación ejecutada.

--- 

### Flujo de Ejecución

Ejemplo: Agendar una cita

```text
HTTP Request
        ↓
ReservationController
        ↓
ScheduleReservationUseCase
        ↓
DoctorRepositoryPort
PatientRepositoryPort
ReservationRepositoryPort
PenaltyRepositoryPort
        ↓
Adapters
        ↓
JPA Repositories
        ↓
PostgreSQL
```

Durante este proceso se validan todas las reglas de negocio definidas en la prueba técnica:

- Existencia del médico
- Existencia del paciente
- Horario válido
- Disponibilidad del médico
- Conflictos del paciente
- Penalizaciones activas

---

### Justificación de la Arquitectura

Se eligió Arquitectura Hexagonal porque:

- Facilita el desacoplamiento entre negocio e infraestructura.
- Permite sustituir PostgreSQL por otro mecanismo de persistencia con cambios mínimos.
- Favorece la aplicación de principios SOLID.
- Facilita la creación de pruebas unitarias mediante mocks de los puertos.
- Mantiene las reglas de negocio independientes de Spring Boot.
- Mejora la mantenibilidad y escalabilidad del sistema.

Para una aplicación orientada a reglas de negocio como un sistema de agendamiento médico, esta arquitectura permite que la complejidad del dominio permanezca aislada de las decisiones tecnológicas.

---

## Ejecución Local

### Prerrequisitos

- Java 21
- PostgreSQL
- Maven 3.9+

### Crear Base de Datos

```sql
CREATE DATABASE medisalud;
```

### Configurar application.yml

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/medisalud
    username: postgres
    password: postgres

  jpa:
    hibernate:
      ddl-auto: validate

  flyway:
    enabled: true
```

### Ejecutar la Aplicación

```bash
./mvnw spring-boot:run
```

o

```bash
mvn spring-boot:run
```

### Ejecutar Tests

```bash
./mvnw test
```

---

## Documentación OpenAPI

Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

---

# Endpoints

## Registrar Médico

### Request

POST /api/v1/doctors

```json
{
  "fullName": "Dra. María González",
  "specialty": "Cardiología",
  "phone": "5551001",
  "email": "maria.gonzalez@medisalud.com"
}
```

### Response

```json
{
  "id": "3b88b3e4-cd8a-4a44-a27c-45b53d2c7c3f",
  "fullName": "Dra. María González",
  "specialty": "Cardiología",
  "phone": "5551001",
  "email": "maria.gonzalez@medisalud.com"
}
```

---

## Registrar Paciente

### Request

POST /api/v1/patients

```json
{
  "fullName": "Juan Pérez",
  "document": "123456789",
  "birthDate": "2000-01-01",
  "phone": "3001234567",
  "email": "juan@email.com"
}
```

### Response

```json
{
  "id": "5f1bb83f-c8a3-4a0d-9c4d-6e84754f8c26",
  "fullName": "Juan Pérez",
  "document": "123456789",
  "phone": "3001234567",
  "email": "juan@email.com",
  "birthDate": "2000-01-01"
}
```

---

## Agendar Cita

### Request

POST /api/v1/reservations

```json
{
  "doctorId": "3b88b3e4-cd8a-4a44-a27c-45b53d2c7c3f",
  "patientId": "5f1bb83f-c8a3-4a0d-9c4d-6e84754f8c26",
  "scheduledAt": "2026-07-20T09:00:00"
}
```

### Response

```json
{
  "id": "7c36cbcb-f95d-4b5c-8ec7-5326d8a85c6c",
  "patientId": "5f1bb83f-c8a3-4a0d-9c4d-6e84754f8c26",
  "doctorId": "3b88b3e4-cd8a-4a44-a27c-45b53d2c7c3f",
  "scheduledAt": "2026-07-20T09:00:00",
  "status": "SCHEDULED"
}
```

---

## Cancelar Cita

### Request

PATCH /api/v1/reservations/{reservationId}/cancel

### Response

```json
{
  "id": "7c36cbcb-f95d-4b5c-8ec7-5326d8a85c6c",
  "status": "CANCELLED",
  "penaltyApplied": false
}
```

---

## Consultar Disponibilidad

### Request

GET /api/v1/doctors/{doctorId}/availability?startDate=2026-07-20&endDate=2026-07-25

### Response

```json
[
  {
    "startAt": "2026-07-20T08:00:00",
    "endAt": "2026-07-20T08:30:00"
  },
  {
    "startAt": "2026-07-20T09:00:00",
    "endAt": "2026-07-20T09:30:00"
  }
]
```

---

## Listar Citas

### Request

GET /api/v1/reservations?doctorId={doctorId}&status=SCHEDULED

### Response

```json
[
  {
    "id": "7c36cbcb-f95d-4b5c-8ec7-5326d8a85c6c",
    "patientId": "5f1bb83f-c8a3-4a0d-9c4d-6e84754f8c26",
    "doctorId": "3b88b3e4-cd8a-4a44-a27c-45b53d2c7c3f",
    "scheduledAt": "2026-07-20T09:00:00",
    "status": "SCHEDULED"
  }
]
```