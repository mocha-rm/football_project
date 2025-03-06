package com.side.football_project.reservation.service;

import com.side.football_project.reservation.dto.ReservationRequestDto;
import com.side.football_project.reservation.dto.ReservationResponseDto;

public interface ReservationService {
    ReservationResponseDto createReservation(ReservationRequestDto requestDto);
    ReservationResponseDto findReservation(Long id);
    void deleteReservation(Long id);
}
