package com.side.football_project.domain.shorts.service;

import com.side.football_project.domain.shorts.dto.ShortsRequestDto;
import com.side.football_project.domain.shorts.dto.ShortsResponseDto;
import com.side.football_project.domain.shorts.entity.Shorts;
import org.springframework.data.domain.Page;

public interface ShortsService {
    ShortsResponseDto createShorts(ShortsRequestDto requestDto);

    ShortsResponseDto findShorts(Long id);

    Page<ShortsResponseDto> findShortsFeed(int page, int size);

    ShortsResponseDto updateShorts(Long id, ShortsRequestDto requestDto);

    void deleteShorts(Long id);

    Shorts getShortsFromDB(Long id);
}
