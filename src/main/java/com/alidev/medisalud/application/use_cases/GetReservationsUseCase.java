package com.alidev.medisalud.application.use_cases;

import com.alidev.medisalud.application.dtos.request.GetReservationsRequest;
import com.alidev.medisalud.application.dtos.response.ReservationSummaryResponse;
import com.alidev.medisalud.application.filters.ReservationFilter;
import com.alidev.medisalud.application.mappers.ReservationDtoMapper;
import com.alidev.medisalud.domain.ports.application.GetReservationsPort;
import com.alidev.medisalud.domain.ports.infrastructure.persistence.ReservationRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetReservationsUseCase implements GetReservationsPort {
    private final ReservationRepositoryPort reservationRepository;

    @Override
    public List<ReservationSummaryResponse> execute(GetReservationsRequest request) {
        return ReservationFilter.apply(reservationRepository.findAll(), request)
                .stream()
                .map(ReservationDtoMapper::toSummaryResponse)
                .toList();
    }
}