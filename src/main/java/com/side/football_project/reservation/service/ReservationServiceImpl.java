package com.side.football_project.reservation.service;

import com.side.football_project.domain.user.entity.User;
import com.side.football_project.reservation.domain.Reservation;
import com.side.football_project.reservation.dto.ReservationRequestDto;
import com.side.football_project.reservation.dto.ReservationResponseDto;
import com.side.football_project.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserService userService;
    private final StadiumService stadiumService;

    @Transactional
    @Override
    public ReservationResponseDto createReservation(ReservationRequestDto requestDto) {

        User user = userService.findById(requestDto.getUser().getId());
        Stadium stadium = stadiumService.findByIdWithLock(requestDto.getStadium().getId());

        if (stadium.isFullyBooked()) {
            throw new IllegalStateException("이미 예약이 완료된 경기장입니다.");
        }

        Reservation reservation = Reservation.builder()
                .name(requestDto.getName())
                .fee(requestDto.getFee())
                .user(user)
                .stadium(stadium)
                .build();

        reservationRepository.save(reservation);

        return ReservationResponseDto.toEntity(reservation);
    }

    @Override
    public ReservationResponseDto findReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 예약이 없습니다. id=" + id));

        return ReservationResponseDto.toEntity(reservation);
    }

    @Transactional
    @Override
    public void deleteReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 예약이 없습니다. id=" + id));

        reservationRepository.delete(reservation);
    }
}
