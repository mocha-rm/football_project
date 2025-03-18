package com.side.football_project.domain.shorts.service;

import com.side.football_project.domain.shorts.dto.ShortsRequestDto;
import com.side.football_project.domain.shorts.dto.ShortsResponseDto;
import com.side.football_project.domain.shorts.entity.Shorts;

public interface ShortsService {
    ShortsResponseDto createShorts(ShortsRequestDto requestDto);

    ShortsResponseDto findShorts(Long id);

    ShortsResponseDto updateShorts(Long id, ShortsRequestDto requestDto);

    void deleteShorts(Long id);

    Shorts getShortsFromDB(Long id);
}
