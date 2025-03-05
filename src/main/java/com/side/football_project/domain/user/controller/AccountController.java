package com.side.football_project.domain.user.controller;

import com.side.football_project.domain.user.dto.UserRequestDto;
import com.side.football_project.domain.user.dto.UserResponseDto;
import com.side.football_project.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signUp(@RequestBody UserRequestDto requestDto) {
        return new ResponseEntity<>(userService.createUser(requestDto), HttpStatus.CREATED);
    }
}
