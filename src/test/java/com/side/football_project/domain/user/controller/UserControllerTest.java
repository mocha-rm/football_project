package com.side.football_project.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.side.football_project.config.TestSecurityConfig;
import com.side.football_project.domain.user.dto.UserPasswordUpdateDto;
import com.side.football_project.domain.user.dto.UserRequestDto;
import com.side.football_project.domain.user.dto.UserResponseDto;
import com.side.football_project.domain.user.service.UserService;
import com.side.football_project.domain.user.type.UserRole;
import com.side.football_project.domain.user.type.UserTier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(TestSecurityConfig.class)
@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @Autowired
    private ObjectMapper objectMapper;

    private UserRequestDto userRequestDto;
    private UserResponseDto userResponseDto;

    @BeforeEach
    void setUp() {
        userRequestDto = UserRequestDto.builder()
                .email("testUser@email.com")
                .password("qwer!1234")
                .name("tester")
                .phoneNumber("01012345678")
                .age(30)
                .role(UserRole.NORMAL)
                .build();

        userResponseDto = UserResponseDto.builder()
                .id(1L)
                .email("testUser@email.com")
                .name("tester")
                .phoneNumber("01012345678")
                .age(30)
                .userTier(UserTier.ROOKIE)
                .userRole(UserRole.NORMAL)
                .build();
    }


    @Test
    void findUser() throws Exception {
        UserResponseDto responseDto = userResponseDto;
        when(userService.findUser(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("tester"));
    }

    @Test
    void updateName() throws Exception {
        UserRequestDto requestDto = UserRequestDto.builder().name("newName").build();
        doNothing().when(userService).updateName(any(UserRequestDto.class));

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("이름이 수정되었습니다."));
    }

    @Test
    void updatePassword() throws Exception {
        UserPasswordUpdateDto passwordUpdateDto = new UserPasswordUpdateDto("oldPass", "newPass", "newPass");
        doNothing().when(userService).updatePassword(any(UserPasswordUpdateDto.class));

        mockMvc.perform(patch("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passwordUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("비밀번호가 수정되었습니다."));
    }

    @Test
    void deleteUser() throws Exception {
        UserRequestDto requestDto = userRequestDto;
        doNothing().when(userService).deleteUser(any(UserRequestDto.class), any());

        mockMvc.perform(delete("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("성공적으로 탈퇴처리 되었습니다."));
    }

    @Test
    void updateTier() throws Exception {
        doNothing().when(userService).updateUserTier(1L);

        mockMvc.perform(patch("/users/{userId}/tier", 1))
                .andExpect(status().isOk())
                .andExpect(content().string("유저 티어가 업데이트 되었습니다."));
    }
}