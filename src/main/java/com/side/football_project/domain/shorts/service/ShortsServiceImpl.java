package com.side.football_project.domain.shorts.service;

import com.side.football_project.domain.shorts.dto.ShortsRequestDto;
import com.side.football_project.domain.shorts.dto.ShortsResponseDto;
import com.side.football_project.domain.shorts.entity.Shorts;
import com.side.football_project.domain.shorts.repository.ShortsRepository;
import com.side.football_project.global.common.exception.CustomException;
import com.side.football_project.global.common.exception.type.ShortsErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShortsServiceImpl implements ShortsService {
    private final ShortsRepository shortsRepository;

    @Override
    public ShortsResponseDto createShorts(ShortsRequestDto requestDto) {
        Shorts shorts = Shorts.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .url(requestDto.getUrl())
                .build();

        shortsRepository.save(shorts);

        return ShortsResponseDto.toDto(shorts);
    }

    @Override
    public ShortsResponseDto findShorts(Long id) {
        Shorts shorts = getShortsFromDB(id);
        return ShortsResponseDto.toDto(shorts);
    }

    @Override
    public ShortsResponseDto updateShorts(Long id, ShortsRequestDto requestDto) {
        Shorts shorts = getShortsFromDB(id);
        shorts.updateShorts(requestDto.getTitle(), requestDto.getDescription());

        shortsRepository.save(shorts);

        return ShortsResponseDto.toDto(shorts);
    }

    @Override
    public void deleteShorts(Long id) {
        shortsRepository.delete(getShortsFromDB(id));
    }

    @Override
    public Shorts getShortsFromDB(Long id) {
        return shortsRepository.findById(id)
                .orElseThrow(() -> new CustomException(ShortsErrorCode.SHORTS_NOT_FOUND));
    }
}
