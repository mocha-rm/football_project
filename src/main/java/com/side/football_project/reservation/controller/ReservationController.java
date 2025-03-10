package com.side.football_project.reservation.controller;

import com.side.football_project.reservation.dto.ReservationDeleteRequestDto;
import com.side.football_project.reservation.dto.ReservationRequestDto;
import com.side.football_project.reservation.dto.ReservationResponseDto;
import com.side.football_project.reservation.service.ReservationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponseDto> createReservation(@RequestBody ReservationRequestDto requestDto) {
        return ResponseEntity.ok(reservationService.createReservation(requestDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDto> findReservation(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.findReservation(id));
    }

    @GetMapping("/stadiums/{id}")
    public ResponseEntity<Page<ReservationResponseDto>> findReservation(@PathVariable Long id,
                                                                        @RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "10") int size) {
        Page<ReservationResponseDto> reservation = reservationService.findAllReservationByStadium(id, page, size);
        return ResponseEntity.ok(reservation);
    }

    @GetMapping("/stadium/{id}")
    public ResponseEntity<ReservationResponseDto> findReservationByStadium(@RequestParam Long id) {
        return ResponseEntity.ok(reservationService.findReservationByStadium(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<ReservationResponseDto>> findReservationByUser(@PathVariable Long userId,
                                                                              @RequestParam(defaultValue = "0") int page,
                                                                              @RequestParam(defaultValue = "10") int size) {
        Page<ReservationResponseDto> reservation = reservationService.findReservationByUser(userId, page, size);
        return ResponseEntity.ok(reservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@RequestBody ReservationDeleteRequestDto requestDto) {
        reservationService.deleteReservation(requestDto);
        return ResponseEntity.noContent().build();
    }
}
