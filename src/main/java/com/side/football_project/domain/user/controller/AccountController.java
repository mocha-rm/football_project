package com.side.football_project.domain.user.controller;

import com.side.football_project.domain.user.dto.UserRequestDto;
import com.side.football_project.domain.user.dto.UserResponseDto;
import com.side.football_project.domain.user.service.UserService;
import com.side.football_project.global.security.auth.CustomUserDetails;
import com.side.football_project.global.security.auth.SessionAuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AccountController {
    private final UserService userService;
    private final SessionAuthenticationService sessionAuthenticationService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signUp(@RequestBody UserRequestDto requestDto) {
        return new ResponseEntity<>(userService.createUser(requestDto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserRequestDto requestDto, HttpServletRequest request) {
        try {
            UserDetails userDetails = sessionAuthenticationService.authenticate(requestDto.getEmail(), requestDto.getPassword(), request);

            if (userDetails instanceof CustomUserDetails customUserDetails) {
                String role = customUserDetails.getUser().getRole().name();
                return new ResponseEntity<>("로그인 성공: 역할 - " + role, HttpStatus.OK);
            }
            return new ResponseEntity<>("로그인 성공", HttpStatus.OK);

        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("이메일 또는 비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        SecurityContextHolder.clearContext();
        log.info("Logout!");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
