package com.side.football_project.domain.user.service;

import com.side.football_project.domain.user.dto.UserRequestDto;
import com.side.football_project.domain.user.dto.UserResponseDto;
import com.side.football_project.domain.user.entity.User;
import com.side.football_project.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto createUser(UserRequestDto requestDto) {
        User user = User.builder()
                .name(requestDto.getName())
                .phoneNumber(requestDto.getPhoneNumber())
                .age(requestDto.getAge())
                .role(requestDto.getUserRole())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .email(requestDto.getEmail())
                .build();

        userRepository.save(user);

        return UserResponseDto.toDto(user);
    }
}
