package com.side.football_project.domain.shorts.service;

import com.side.football_project.domain.shorts.dto.ShortsRequestDto;
import com.side.football_project.domain.shorts.dto.ShortsResponseDto;
import com.side.football_project.domain.shorts.entity.Shorts;
import com.side.football_project.domain.shorts.repository.ShortsRepository;
import com.side.football_project.domain.user.entity.User;
import com.side.football_project.domain.user.service.UserService;
import com.side.football_project.global.common.exception.CustomException;
import com.side.football_project.global.common.exception.type.ShortsErrorCode;
import com.side.football_project.global.common.exception.type.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ShortsServiceImpl implements ShortsService {
    private final ShortsRepository shortsRepository;
    private final UserService userService;

    @Override
    public ShortsResponseDto createShorts(ShortsRequestDto requestDto) {
        Shorts shorts = Shorts.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .url(requestDto.getUrl())
                .user(userService.getLoginUser())
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
    public Page<ShortsResponseDto> findShortsFeed(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Shorts> shortsPage = shortsRepository.findAll(pageable);
        return shortsPage.map(ShortsResponseDto::toDto);
    }

    @Override
    public ShortsResponseDto updateShorts(Long id, ShortsRequestDto requestDto) {
        Shorts shorts = checkAuthorityForEdit(id);

        shorts.updateShorts(requestDto.getTitle(), requestDto.getDescription());

        shortsRepository.save(shorts);

        return ShortsResponseDto.toDto(shorts);
    }

    @Override
    public void deleteShorts(Long id) {
        shortsRepository.delete(checkAuthorityForEdit(id));
    }

    @Override
    public Shorts getShortsFromDB(Long id) {
        return shortsRepository.findById(id)
                .orElseThrow(() -> new CustomException(ShortsErrorCode.SHORTS_NOT_FOUND));
    }

    private Shorts checkAuthorityForEdit(Long shortsId) {
        User user = userService.getLoginUser();
        Shorts shorts = getShortsFromDB(shortsId);

        if (!Objects.equals(user.getId(), shorts.getUser().getId())) {
            throw new CustomException(UserErrorCode.NOT_ALLOWED);
        }

        return shorts;
    }
}
