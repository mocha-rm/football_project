package com.side.football_project.domain.stadium.service;

import com.side.football_project.domain.stadium.dto.StadiumRequestDto;
import com.side.football_project.domain.stadium.dto.StadiumResponseDto;
import com.side.football_project.domain.stadium.dto.StadiumUpdateDto;
import com.side.football_project.domain.stadium.entity.Stadium;

public interface StadiumService {
    StadiumResponseDto createStadium(StadiumRequestDto requestDto);
    StadiumResponseDto findStadium(Long id);
    StadiumUpdateDto updateStadium(StadiumUpdateDto requestDto);
    Stadium findByIdWithLock(Long id);
    void deleteStadium(Long id);
}
