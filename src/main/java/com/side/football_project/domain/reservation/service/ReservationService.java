package com.side.football_project.domain.reservation.service;

import com.side.football_project.domain.reservation.dto.ReservationDeleteRequestDto;
import com.side.football_project.domain.reservation.dto.ReservationRequestDto;
import com.side.football_project.domain.reservation.dto.ReservationResponseDto;
import org.springframework.data.domain.Page;

public interface ReservationService {
    ReservationResponseDto createReservation(ReservationRequestDto requestDto);

    ReservationResponseDto findReservation(Long id);

    Page<ReservationResponseDto> findAllReservationByStadium(Long id, int page, int size);

    ReservationResponseDto findReservationByStadium(Long id);

    Page<ReservationResponseDto> findReservationByUser(Long userId, int page, int size);

    void deleteReservation(ReservationDeleteRequestDto requestDto);

}
