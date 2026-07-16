package com.alidev.medisalud.infrastructure.persistence.adapters;

import com.alidev.medisalud.domain.entities.Reservation;
import com.alidev.medisalud.domain.ports.infrastructure.persistence.ReservationRepositoryPort;
import com.alidev.medisalud.infrastructure.persistence.mappers.ReservationMapper;
import com.alidev.medisalud.infrastructure.persistence.repositories.SpringDataReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryAdapter implements ReservationRepositoryPort {
    private final SpringDataReservationRepository repository;

    @Override
    public Reservation save(Reservation reservation) {
        return ReservationMapper.toDomain(
                repository.save(
                        ReservationMapper.toJpaEntity(reservation)
                )
        );
    }

    @Override
    public Optional<Reservation> findById(UUID reservationId) {
        return repository.findById(reservationId).map(ReservationMapper::toDomain);
    }

    @Override
    public List<Reservation> findByPatientId(UUID patientId) {
        return repository.findByPatientId(patientId)
                .stream()
                .map(ReservationMapper::toDomain)
                .toList();
    }

    @Override
    public List<Reservation> findByDoctorId(UUID doctorId) {
        return repository.findByDoctorId(doctorId)
                .stream()
                .map(ReservationMapper::toDomain)
                .toList();
    }

    @Override
    public List<Reservation> findAll() {
        return repository.findAll()
                .stream()
                .map(ReservationMapper::toDomain)
                .toList();
    }
}