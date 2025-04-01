package com.side.football_project.domain.user.service;

import com.side.football_project.domain.user.dto.UserPasswordUpdateDto;
import com.side.football_project.domain.user.dto.UserRequestDto;
import com.side.football_project.domain.user.dto.UserResponseDto;
import com.side.football_project.domain.user.entity.User;
import com.side.football_project.domain.user.repository.UserRepository;
import com.side.football_project.domain.user.type.UserRole;
import com.side.football_project.global.common.exception.CustomException;
import com.side.football_project.global.security.auth.CustomUserDetails;
import com.side.football_project.global.security.auth.SessionAuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SessionAuthenticationService sessionAuthenticationService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private UserRequestDto userRequestDto;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .email("test@email.com")
                .password("encodedPassword")
                .build();

        userRequestDto = new UserRequestDto(
                "test@email.com",
                "qwer!1234",
                "tester",
                "01012345678",
                100, UserRole.NORMAL);
    }

    @Test
    void createUser() {
        UserRequestDto requestDto = userRequestDto;
        when(userRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(requestDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        UserResponseDto response = userService.createUser(requestDto);
        assertEquals(requestDto.getEmail(), response.getEmail());
    }

    @Test
    void login() {
        UserRequestDto requestDto = userRequestDto;
        when(userRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(requestDto.getPassword(), testUser.getPassword())).thenReturn(true);

        assertDoesNotThrow(() -> userService.login(requestDto, request));
    }

    @Test
    void login_UserNotFound() {
        UserRequestDto requestDto = new UserRequestDto(
                "notfound@example.com",
                "password",
                "name",
                "123456789",
                30,
                UserRole.NORMAL);
        when(userRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> userService.login(requestDto, request));
    }

    @Test
    void findUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        UserResponseDto response = userService.findUser(1L);

        assertEquals(testUser.getEmail(), response.getEmail());
    }

    @Test
    void updateName() {
        mockAuthentication();
        UserRequestDto requestDto = UserRequestDto.builder().name("newName").build();
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        assertDoesNotThrow(() -> userService.updateName(requestDto));
    }

    @Test
    void updatePassword() {
        mockAuthentication();
        UserPasswordUpdateDto passwordUpdateDto = new UserPasswordUpdateDto(
                "oldPassword",
                "newPassword",
                "newPassword");
        when(passwordEncoder.matches(passwordUpdateDto.getCurrentPassword(), testUser.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(passwordUpdateDto.getNewPassword())).thenReturn("encodedNewPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        assertDoesNotThrow(() -> userService.updatePassword(passwordUpdateDto));
    }

    @Test
    void deleteUser() {
        mockAuthentication();
        HttpSession session = mock(HttpSession.class);
        when(passwordEncoder.matches("password", testUser.getPassword())).thenReturn(true);
        doNothing().when(userRepository).delete(any(User.class));

        assertDoesNotThrow(() -> userService.deleteUser(UserRequestDto.builder().password("password").build(), session));
    }

    @Test
    void getAllUser() {
        when(userRepository.findAll()).thenReturn(List.of(testUser));
        List<User> users = userService.getAllUser();

        assertFalse(users.isEmpty());
    }

    private void mockAuthentication() {
        Authentication authentication = mock(Authentication.class);
        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(userDetails.getUser()).thenReturn(testUser);
    }
}