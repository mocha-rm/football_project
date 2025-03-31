package com.side.football_project.domain.user.controller;

import com.side.football_project.domain.user.dto.UserPasswordUpdateDto;
import com.side.football_project.domain.user.dto.UserRequestDto;
import com.side.football_project.domain.user.dto.UserResponseDto;
import com.side.football_project.domain.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    /**
     * 특정 유저 조회
     * @param userId 유저 ID
     * @return 유저 정보 {@link UserResponseDto}
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> findUser(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.findUser(userId), HttpStatus.OK);
    }

    /**
     * 이름 수정
     * @param requestDto 이름 수정에 필요한 데이터
     * @return 수정 완료 메시지
     */
    @PutMapping
    public ResponseEntity<String> updateName(@RequestBody UserRequestDto requestDto) {
        userService.updateName(requestDto);
        return ResponseEntity.ok("이름이 수정되었습니다.");
    }

    /**
     * 비밀번호 수정
     * @param passwordUpdateDto 비밀번호 수정에 필요한 데이터
     * @return 수정 완료 메시지
     */
    @PatchMapping
    public ResponseEntity<String> updatePassword(@RequestBody UserPasswordUpdateDto passwordUpdateDto) {
        userService.updatePassword(passwordUpdateDto);
        return ResponseEntity.ok("비밀번호가 수정되었습니다.");
    }

    /**
     * 유저 삭제
     * @param requestDto 유저 삭제에 필요한 데이터
     * @param session 세션 정보
     * @return 유저 삭제 메시지
     */
    @DeleteMapping
    public ResponseEntity<String> deleteUser(@RequestBody UserRequestDto requestDto, HttpSession session) {
        userService.deleteUser(requestDto, session);
        return ResponseEntity.ok("성공적으로 탈퇴처리 되었습니다.");
    }

    /**
     * 유저 티어 업데이트
     * @param userId 유저 ID
     * @return 티어 업데이트 완료 메시지
     */
    @PatchMapping("/{userId}/tier")
    public ResponseEntity<String> updateTier(@PathVariable Long userId) {
        userService.updateUserTier(userId);
        return ResponseEntity.ok("유저 티어가 업데이트 되었습니다.");
    }
}
