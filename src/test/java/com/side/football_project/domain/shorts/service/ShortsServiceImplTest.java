package com.side.football_project.domain.shorts.service;

import com.side.football_project.domain.shorts.dto.ShortsRequestDto;
import com.side.football_project.domain.shorts.dto.ShortsResponseDto;
import com.side.football_project.domain.shorts.entity.Shorts;
import com.side.football_project.domain.shorts.repository.ShortsRepository;
import com.side.football_project.domain.user.entity.User;
import com.side.football_project.domain.user.service.UserService;
import com.side.football_project.domain.user.type.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShortsServiceImplTest {
    @Mock
    private ShortsRepository shortsRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ShortsServiceImpl shortsService;

    private User user;
    private Shorts shorts;
    private ShortsRequestDto shortsRequestDto;


    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("tester")
                .phoneNumber("01012345678")
                .age(20)
                .role(UserRole.NORMAL)
                .password("password")
                .email("tester@email.com")
                .build();

        shorts = Shorts.builder()
                .title("test shorts")
                .description("test description")
                .url("https://s3.test.com/video.mp4")
                .user(user)
                .build();

        ReflectionTestUtils.setField(shorts, "id", 1L);

        shortsRequestDto = ShortsRequestDto.builder()
                .title("request title")
                .description("request description")
                .url("https://s3.test.com/request_video.mp4")
                .build();
    }

    @Test
    void createShorts() {
        when(userService.getLoginUser()).thenReturn(user);
        when(shortsRepository.save(any(Shorts.class))).thenReturn(shorts);

        ShortsResponseDto responseDto = shortsService.createShorts(shortsRequestDto);

        assertThat(responseDto.getTitle()).isEqualTo(shortsRequestDto.getTitle());
        assertThat(responseDto.getDescription()).isEqualTo(shortsRequestDto.getDescription());
    }

    @Test
    void findShorts() {
        when(shortsRepository.findById(1L)).thenReturn(Optional.of(shorts));

        ShortsResponseDto responseDto = shortsService.findShorts(1L);
        assertThat(responseDto.getId()).isEqualTo(1L);
    }

    @Test
    void findShortsFeed() {
        List<Shorts> shortsList = List.of(shorts);
        Page<Shorts> page = new PageImpl<>(
                shortsList,
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt")),
                shortsList.size());
        when(shortsRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<ShortsResponseDto> responsePage = shortsService.findShortsFeed(0, 10);
        assertThat(responsePage.getTotalElements()).isEqualTo(1);
    }

    @Test
    void updateShorts() {
        when(userService.getLoginUser()).thenReturn(user);
        when(shortsRepository.findById(1L)).thenReturn(Optional.of(shorts));
        when(shortsRepository.save(any(Shorts.class))).thenReturn(shorts);

        ShortsResponseDto responseDto = shortsService.updateShorts(1L, shortsRequestDto);
        assertThat(responseDto.getTitle()).isEqualTo(shortsRequestDto.getTitle());
    }

    @Test
    void deleteShorts() {
        when(userService.getLoginUser()).thenReturn(user);
        when(shortsRepository.findById(1L)).thenReturn(Optional.of(shorts));

        shortsService.deleteShorts(1L);

        verify(shortsRepository, times(1)).delete(shorts);
    }

    @Test
    void getShortsFromDB() {
        when(shortsRepository.findById(1L)).thenReturn(Optional.of(shorts));

        Shorts foundShorts = shortsService.getShortsFromDB(1L);
        assertThat(foundShorts.getId()).isEqualTo(1L);
    }
}