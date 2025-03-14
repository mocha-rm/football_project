package com.side.football_project.domain.user.service;

import com.side.football_project.domain.user.dto.UserPasswordUpdateDto;
import com.side.football_project.domain.user.dto.UserRequestDto;
import com.side.football_project.domain.user.dto.UserResponseDto;
import com.side.football_project.domain.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.List;

public interface UserService {
    UserResponseDto createUser(UserRequestDto requestDto);

    void login(UserRequestDto requestDto, HttpServletRequest request);

    UserResponseDto findUser(Long userId);

    void updateName(UserRequestDto requestDto);

    void updatePassword(UserPasswordUpdateDto passwordUpdateDto);

    void deleteUser(UserRequestDto requestDto, HttpSession session);

    User getUserFromDB(Long userId);

    User getLoginUser();

    void updateUserTier(Long userId);

    List<User> getAllUser();
}
