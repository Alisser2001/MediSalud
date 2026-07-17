package com.alidev.medisalud.infrastructure.persistence.adapters;

import com.alidev.medisalud.domain.entities.Reservation;
import com.alidev.medisalud.domain.ports.infrastructure.persistence.ReservationRepositoryPort;
import com.alidev.medisalud.infrastructure.exceptions.RepositoryException;
import com.alidev.medisalud.infrastructure.persistence.mappers.ReservationMapper;
import com.alidev.medisalud.infrastructure.persistence.repositories.SpringDataReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
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
        try{
            return ReservationMapper.toDomain(
                    repository.save(
                            ReservationMapper.toJpaEntity(reservation)
                    )
            );
        } catch (DataAccessException ex) {
            throw new RepositoryException("Error saving reservation.", ex);
        }
    }

    @Override
    public Optional<Reservation> findById(UUID reservationId) {
        try{
            return repository.findById(reservationId).map(ReservationMapper::toDomain);
        } catch (DataAccessException ex) {
            throw new RepositoryException(
                    "Error retrieving reservation.",
                    ex
            );
        }
    }

    @Override
    public List<Reservation> findByPatientId(UUID patientId) {
        try{
            return repository.findByPatientId(patientId)
                    .stream()
                    .map(ReservationMapper::toDomain)
                    .toList();
        } catch (DataAccessException ex) {
            throw new RepositoryException("Error retrieving reservations by patient.", ex);
        }
    }

    @Override
    public List<Reservation> findByDoctorId(UUID doctorId) {
        try{
            return repository.findByDoctorId(doctorId)
                    .stream()
                    .map(ReservationMapper::toDomain)
                    .toList();
        } catch (DataAccessException ex) {
            throw new RepositoryException("Error retrieving reservations by doctor.", ex);
        }
    }

    @Override
    public List<Reservation> findAll() {
        try{
            return repository.findAll()
                    .stream()
                    .map(ReservationMapper::toDomain)
                    .toList();
        } catch (DataAccessException ex) {
            throw new RepositoryException("Error retrieving reservations.", ex);
        }
    }
}