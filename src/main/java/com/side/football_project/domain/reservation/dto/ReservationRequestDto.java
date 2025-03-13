package com.side.football_project.domain.reservation.dto;

import com.side.football_project.domain.stadium.entity.Stadium;
import com.side.football_project.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class ReservationRequestDto {

    private String name;
    private BigDecimal fee;
    private User user;
    private Long stadiumId;

    public ReservationRequestDto(String name, BigDecimal fee, User user, Long stadiumId) {
        this.name = name;
        this.fee = fee;
        this.user = user;
        this.stadiumId = stadiumId;
    }
}
