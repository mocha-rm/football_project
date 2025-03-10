package com.side.football_project.reservation.service;

import com.side.football_project.domain.user.dto.UserResponseDto;
import com.side.football_project.domain.user.service.UserService;
import com.side.football_project.global.security.auth.SessionAuthenticationService;
import com.side.football_project.reservation.domain.Reservation;
import com.side.football_project.reservation.dto.ReservationRequestDto;
import com.side.football_project.reservation.dto.ReservationResponseDto;
import com.side.football_project.reservation.repository.ReservationRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserService userService;
    private final SessionAuthenticationService sessionAuthenticationService;
    private final StadiumService stadiumService;

    @Transactional
    @Override
    public ReservationResponseDto createReservation(ReservationRequestDto requestDto, HttpServletRequest request) {

        sessionAuthenticationService.authenticate(requestDto.getUser().getEmail(), requestDto.getUser().getPassword(), request);

        UserResponseDto user = userService.findUser(requestDto.getUser().getId());
        Stadium stadium = stadiumService.findByIdWithLock(requestDto.getStadium().getId());

        if (stadium.isFullyBooked()) {
            throw new IllegalStateException("이미 예약이 완료된 경기장입니다.");
        }

        Reservation reservation = Reservation.builder()
                .name(requestDto.getName())
                .fee(requestDto.getFee())
                .user(UserResponseDto.toEntity(user))
                .stadium(stadium)
                .build();

        reservationRepository.save(reservation);

        return ReservationResponseDto.toEntity(reservation);
    }

    @Override
    public ReservationResponseDto findReservation(Long id) {
        Reservation reservation = reservationRepository.findReservation(id);
        return ReservationResponseDto.toEntity(reservation);
    }

    @Override
    public Page<ReservationResponseDto> findAllReservationByStadium(Long id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Reservation> reservation = reservationRepository.findReservationByStadium(id, pageable);
        return reservation.map(ReservationResponseDto::toEntity);
    }

    @Override
    public ReservationResponseDto findReservationByStadium(Long stadiumId) {
        Reservation reservation = reservationRepository.findReservationByStadiumId(stadiumId)
                .orElseThrow(() -> new IllegalArgumentException("해당 경기장에 예약된 내역이 없습니다. id=" + stadiumId));

        return ReservationResponseDto.toEntity(reservation);
    }

    @Override
    public Page<ReservationResponseDto> findReservationByUser(Long id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Reservation> reservation = reservationRepository.findReservationByUser(id, pageable);
        return reservation.map(ReservationResponseDto::toEntity);
    }

    @Transactional
    @Override
    public void deleteReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 예약이 없습니다. id=" + id));

        reservationRepository.delete(reservation);
    }
}
