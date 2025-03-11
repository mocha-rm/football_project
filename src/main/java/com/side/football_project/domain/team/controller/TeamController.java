package com.side.football_project.domain.team.controller;

import com.side.football_project.domain.team.dto.TeamRequestDto;
import com.side.football_project.domain.team.dto.TeamResponseDto;
import com.side.football_project.domain.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/team")
public class TeamController {
    private final TeamService teamService;

    /**
     * 팀 생성
     *
     * @param requestDto 팀 생성에 필요한 데이터
     * @return 생성된 팀 정보
     */
    @PostMapping
    public ResponseEntity<TeamResponseDto> createTeam(@RequestBody TeamRequestDto requestDto) {
        return new ResponseEntity<>(teamService.createTeam(requestDto), HttpStatus.CREATED);
    }

    /**
     * 팀 조회
     * @param teamId 팀 ID
     * @return 조회된 팀 정보
     */
    @GetMapping("/{teamId}")
    public ResponseEntity<TeamResponseDto> findTeam(@PathVariable Long teamId) {
        return new ResponseEntity<>(teamService.findTeam(teamId), HttpStatus.OK);
    }

    /**
     * 팀 수정
     * @param teamId 팀 ID
     * @param requestDto 팀 수정에 필요한 데이터
     * @return 팀 수정 완료 메시지
     */
    @PutMapping("/{teamId}")
    public ResponseEntity<String> updateTeam(@PathVariable Long teamId, @RequestBody TeamRequestDto requestDto) {
        return new ResponseEntity<>(teamService.updateTeam(teamId, requestDto), HttpStatus.OK);
    }

    /**
     * 팀 삭제
     * @param teamId 팀 ID
     * @return 팀 삭제 완료 메시지
     */
    @DeleteMapping("/{teamId}")
    public ResponseEntity<String> deleteTeam(@PathVariable Long teamId) {
        return new ResponseEntity<>(teamService.deleteTeam(teamId), HttpStatus.OK);
    }
}
