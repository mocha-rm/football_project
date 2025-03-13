package com.side.football_project.domain.team.service;

import com.side.football_project.domain.team.dto.TeamRequestDto;
import com.side.football_project.domain.team.dto.TeamResponseDto;
import com.side.football_project.domain.team.entity.Team;

public interface TeamService {
    TeamResponseDto createTeam(TeamRequestDto requestDto);

    TeamResponseDto findTeam(Long teamId);

    void updateTeam(Long teamId, TeamRequestDto requestDto);

    void deleteTeam(Long teamId);

    Team getTeamFromDB(Long teamId);
}
