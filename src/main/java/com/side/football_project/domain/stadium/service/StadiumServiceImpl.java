package com.side.football_project.domain.stadium.service;

import com.side.football_project.domain.stadium.dto.StadiumRequestDto;
import com.side.football_project.domain.stadium.dto.StadiumResponseDto;
import com.side.football_project.domain.stadium.dto.StadiumUpdateDto;
import com.side.football_project.domain.stadium.entity.Address;
import com.side.football_project.domain.stadium.entity.StadiumStatus;
import com.side.football_project.domain.stadium.entity.Stadium;
import com.side.football_project.domain.stadium.repository.StadiumRepository;
import com.side.football_project.domain.user.entity.User;
import com.side.football_project.domain.user.service.UserService;
import com.side.football_project.domain.user.type.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StadiumServiceImpl implements StadiumService {

    private final StadiumRepository stadiumRepository;
    private final UserService userService;


    //Address request에 추가 및 수정
    @Transactional
    @Override
    public StadiumResponseDto createStadium(StadiumRequestDto requestDto) {
        User user = userService.getLoginUser();
        if (user.getRole() != UserRole.VENDOR) {
            throw new IllegalStateException("구장 사업주가 아닙니다.");
        }

        Stadium stadium = Stadium.builder()
                .name(requestDto.getName())
                .status(StadiumStatus.AVAILABLE)
                .description(requestDto.getDescription())
                .user(user)
                .build();

        stadium.applyAddress(new Address(requestDto.getCity(), requestDto.getState(), requestDto.getPostalCode(), requestDto.getSpecificAddress()));

        stadiumRepository.save(stadium);
        return StadiumResponseDto.toEntity(stadium);
    }

    @Override
    public StadiumResponseDto findStadium(Long id) {
        Stadium stadium = stadiumRepository.findById(id).orElseThrow(() -> new IllegalStateException("경기장을 조회할 수 없습니다."));

        return StadiumResponseDto.toEntity(stadium);
    }

    @Override
    public StadiumUpdateDto updateStadium(StadiumUpdateDto requestDto) {
        User findUser = userService.getLoginUser();
        Stadium stadium = stadiumRepository.findById(requestDto.getId()).orElseThrow(() -> new IllegalStateException("경기장을 조회할 수 없습니다."));

        if(!Objects.equals(findUser.getId(), stadium.getUser().getId())) {
            throw new IllegalStateException("경기장 관리자가 아닌 사람은 수정할 수 없습니다.");
        }

        stadium = Stadium.builder()
                .name(requestDto.getName())
                .status(StadiumStatus.AVAILABLE)
                .description(requestDto.getDescription())
                .user(findUser)
                .build();

        stadium.applyAddress(new Address(requestDto.getCity(), requestDto.getState(), requestDto.getPostalCode(), requestDto.getSpecificAddress()));


        return StadiumUpdateDto.toEntity(stadium);
    }

    @Override
    public void deleteStadium(Long id) {
        Stadium stadium = stadiumRepository.findById(id).orElseThrow(() -> new IllegalStateException("경기장을 조회할 수 없습니다."));
        stadiumRepository.deleteById(stadium.getId());
    }

    @Override
    public Stadium findByIdWithLock(Long id) {
        return stadiumRepository.findByIdWithLock(id).orElseThrow(() -> new IllegalStateException("찾을 수 없는 경기장입니다."));
    }
}
