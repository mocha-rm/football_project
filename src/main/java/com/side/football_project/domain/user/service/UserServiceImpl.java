package com.side.football_project.domain.user.service;

import com.side.football_project.domain.user.dto.UserPasswordUpdateDto;
import com.side.football_project.domain.user.dto.UserRequestDto;
import com.side.football_project.domain.user.dto.UserResponseDto;
import com.side.football_project.domain.user.entity.User;
import com.side.football_project.domain.user.repository.UserRepository;
import com.side.football_project.domain.user.type.UserTier;
import com.side.football_project.global.common.exception.CustomException;
import com.side.football_project.global.common.exception.type.UserErrorCode;
import com.side.football_project.global.security.auth.CustomUserDetails;
import com.side.football_project.global.security.auth.SessionAuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final SessionAuthenticationService sessionAuthenticationService;
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

    @Override
    public String login(UserRequestDto requestDto, HttpServletRequest request) {
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        validatePassword(requestDto.getPassword(), user.getPassword());

        sessionAuthenticationService.authenticate(requestDto.getEmail(), requestDto.getPassword(), request);

        return "로그인 성공 : 역할 - " + sessionAuthenticationService.getUserRole().name();
    }

    @Override
    public UserResponseDto findUser(Long userId) {
        User findUser = getUserFromDB(userId);
        return UserResponseDto.toDto(findUser);
    }

    @Override
    public String updateName(UserRequestDto requestDto) {
        User user = getLoginUser();

        user.updateName(requestDto.getName());

        userRepository.save(user);

        return "이름이 수정되었습니다.";
    }

    @Override
    public String updatePassword(UserPasswordUpdateDto passwordUpdateDto) {
        User user = getLoginUser();

        validatePassword(passwordUpdateDto.getCurrentPassword(), user.getPassword());

        if (passwordUpdateDto.getCurrentPassword().equals(passwordUpdateDto.getNewPassword())) {
            throw new CustomException(UserErrorCode.PASSWORD_DUPLICATED);
        } else if (!passwordUpdateDto.getNewPassword().equals(passwordUpdateDto.getNewPasswordConfirm())) {
            throw new CustomException(UserErrorCode.PASSWORD_NOT_CONFIRM);
        }

        user.updatePassword(passwordEncoder.encode(passwordUpdateDto.getNewPassword()));
        userRepository.save(user);

        return "비밀번호가 수정되었습니다.";
    }

    @Override
    public String deleteUser(UserRequestDto requestDto, HttpSession session) {
        User user = getLoginUser();

        validatePassword(requestDto.getPassword(), user.getPassword());
        userRepository.delete(user);

        clearSession(session);

        return "성공적으로 탈퇴처리 되었습니다.";
    }

    /**
     * 유저의 평균 평점을 기반으로 UserTier 업데이트
     */
    @Transactional
    @Override
    public void updateUserTier(Long userId) {
        User user = getUserFromDB(userId);
        Double avgRate = userRepository.findAvgRateByUserId(userId);

        // 평점이 없는 경우 기본값 설정
        UserTier newTier = (avgRate == null) ? UserTier.ROOKIE : UserTier.determineTier(avgRate);
        user.updateTier(newTier);
        userRepository.save(user);
    }



    @Override
    public User getUserFromDB(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
    }

    @Override
    public User getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();

        return principal.getUser();
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new CustomException(UserErrorCode.PASSWORD_MISMATCH);
        }
    }

    private void clearSession(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }

        SecurityContextHolder.clearContext();
    }
}
