package com.side.football_project.domain.user.controller;

import com.side.football_project.domain.user.dto.UserRequestDto;
import com.side.football_project.domain.user.dto.UserResponseDto;
import com.side.football_project.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
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

    /**
     * 회원 가입
     * @param requestDto 회원 가입에 필요한 데이터 {@link UserRequestDto}
     * @return 가입된 회원 정보 {@link UserResponseDto}
     */
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signUp(@RequestBody UserRequestDto requestDto) {
        return new ResponseEntity<>(userService.createUser(requestDto), HttpStatus.CREATED);
    }

    /**
     * 로그인
     * @param requestDto 로그인에 필요한 데이터
     * @param request HttpServletRequest 객체
     * @return 로그인 완료 메시지
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserRequestDto requestDto, HttpServletRequest request) {
        return new ResponseEntity<>(userService.login(requestDto, request), HttpStatus.OK);
    }
}
