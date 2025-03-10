package com.side.football_project.reservation.service;

import com.side.football_project.reservation.dto.ReservationRequestDto;
import com.side.football_project.reservation.dto.ReservationResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

public interface ReservationService {
    ReservationResponseDto createReservation(ReservationRequestDto requestDto, HttpServletRequest request);

    ReservationResponseDto findReservation(Long id);

    Page<ReservationResponseDto> findAllReservationByStadium(Long id, int page, int size);

    ReservationResponseDto findReservationByStadium(Long id);

    Page<ReservationResponseDto> findReservationByUser(Long id, int page, int size);

    void deleteReservation(Long id);
}
