package com.side.football_project.domain.user.service;

import com.side.football_project.domain.user.dto.UserRequestDto;
import com.side.football_project.domain.user.dto.UserResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {
    UserResponseDto createUser(UserRequestDto requestDto);

    String login(UserRequestDto requestDto, HttpServletRequest request);
}
