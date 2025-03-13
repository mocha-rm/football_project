package com.side.football_project.domain.match.controller;


import com.side.football_project.domain.match.dto.MatchRatingRequestDto;
import com.side.football_project.domain.match.dto.MatchRequestDto;
import com.side.football_project.domain.match.dto.MatchResponseDto;
import com.side.football_project.domain.match.service.MatchUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchUserService matchUserService;

    @PostMapping
    public ResponseEntity<MatchResponseDto> create(@RequestBody MatchRequestDto requestDto) {
        return ResponseEntity.ok(matchUserService.createMatch(requestDto));
    }

    @PostMapping("/{matchId}/ratings")
    public ResponseEntity<String> rate(@PathVariable Long matchId,
                                       @RequestBody List<MatchRatingRequestDto> ratings) {
        matchUserService.submitMatchRatings(matchId, ratings);
        return ResponseEntity.ok("평가가 완료되었습니다.");
    }

    @PostMapping("/{matchId}/complete")
    public ResponseEntity<String> complete(@PathVariable Long matchId) {
        matchUserService.completeMatch(matchId);
        return ResponseEntity.ok("경기가 종료되었습니다.");
    }
}
