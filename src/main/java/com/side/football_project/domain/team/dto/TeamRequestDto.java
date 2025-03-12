package com.side.football_project.domain.team.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TeamRequestDto {
    private final String teamName;

    private final int headCount;


    @Builder
    public TeamRequestDto(String teamName, int headCount) {
        this.teamName = teamName;
        this.headCount = headCount;
    }
}
