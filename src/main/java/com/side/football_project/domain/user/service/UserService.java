package com.side.football_project.domain.user.service;

import com.side.football_project.domain.user.dto.UserPasswordUpdateDto;
import com.side.football_project.domain.user.dto.UserRequestDto;
import com.side.football_project.domain.user.dto.UserResponseDto;
import com.side.football_project.domain.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public interface UserService {
    UserResponseDto createUser(UserRequestDto requestDto);

    String login(UserRequestDto requestDto, HttpServletRequest request);

    UserResponseDto findUser(Long userId);

    String updateName(UserRequestDto requestDto);

    String updatePassword(UserPasswordUpdateDto passwordUpdateDto);

    String deleteUser(UserRequestDto requestDto, HttpSession session);

    User getUserFromDB(Long userId);

    User getLoginUser();
}
