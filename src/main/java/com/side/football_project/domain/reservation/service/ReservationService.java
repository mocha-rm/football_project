package com.side.football_project.domain.reservation.service;

import com.side.football_project.domain.reservation.dto.ReservationRequestDto;
import com.side.football_project.domain.reservation.dto.ReservationResponseDto;

public interface ReservationService {
    ReservationResponseDto createReservation(ReservationRequestDto requestDto);
    ReservationResponseDto findReservation(Long id);
    void deleteReservation(Long id);
}
