package com.side.football_project.domain.user.service;

import com.side.football_project.domain.user.dto.UserRequestDto;
import com.side.football_project.domain.user.dto.UserResponseDto;

public interface UserService {
    UserResponseDto createUser(UserRequestDto requestDto);
}
