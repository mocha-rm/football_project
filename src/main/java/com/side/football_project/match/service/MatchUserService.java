package com.side.football_project.match.service;

import com.side.football_project.match.dto.MatchRatingRequestDto;
import com.side.football_project.match.dto.MatchRequestDto;
import com.side.football_project.match.dto.MatchResponseDto;

import java.util.List;

public interface MatchUserService {
    void completeMatch(Long matchId);
    void submitMatchRatings(Long matchId, List<MatchRatingRequestDto> ratings);
    MatchResponseDto createMatch (MatchRequestDto requestDto);
}
