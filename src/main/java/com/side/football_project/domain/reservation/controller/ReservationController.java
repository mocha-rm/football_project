package com.side.football_project.domain.reservation.controller;

import com.side.football_project.domain.reservation.dto.ReservationRequestDto;
import com.side.football_project.domain.reservation.dto.ReservationResponseDto;
import com.side.football_project.domain.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<ReservationResponseDto> findReservation(@RequestParam Long id) {
        return ResponseEntity.ok(reservationService.findReservation(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@RequestParam Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
