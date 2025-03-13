package com.side.football_project.domain.team.service;

import com.side.football_project.domain.team.dto.TeamRequestDto;
import com.side.football_project.domain.team.dto.TeamResponseDto;
import com.side.football_project.domain.team.entity.Team;
import com.side.football_project.domain.team.repository.TeamRepository;
import com.side.football_project.domain.user.entity.User;
import com.side.football_project.domain.user.service.UserService;
import com.side.football_project.global.common.exception.CustomException;
import com.side.football_project.global.common.exception.type.TeamErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final UserService userService;

    @Override
    public TeamResponseDto createTeam(TeamRequestDto requestDto) {
        Team team = Team.builder()
                .teamName(requestDto.getTeamName())
                .headCount(requestDto.getHeadCount())
                .user(userService.getLoginUser())
                .build();

        teamRepository.save(team);

        return TeamResponseDto.toDto(team);
    }

    @Override
    public TeamResponseDto findTeam(Long teamId) {
        Team team = getTeamFromDB(teamId);
        return TeamResponseDto.toDto(team);
    }

    @Override
    public void updateTeam(Long teamId, TeamRequestDto requestDto) {
        Team team = getTeamFromDB(teamId);

        validateAuthorization(team.getId());

        team.updateTeamInfo(requestDto.getTeamName(), requestDto.getHeadCount());

        teamRepository.save(team);
    }

    @Override
    public void deleteTeam(Long teamId) {
        Team team = getTeamFromDB(teamId);

        validateAuthorization(team.getId());

        teamRepository.delete(team);
    }

    @Override
    public Team getTeamFromDB(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(TeamErrorCode.TEAM_NOT_FOUND));
    }

    private void validateAuthorization(Long teamId) {
        Team team = getTeamFromDB(teamId);
        User user = userService.getLoginUser();

        if (!(Objects.equals(team.getUser().getId(), user.getId()))) {
            throw new CustomException(TeamErrorCode.TEAM_UPDATE_NOT_ALLOWED);
        }
    }
}
