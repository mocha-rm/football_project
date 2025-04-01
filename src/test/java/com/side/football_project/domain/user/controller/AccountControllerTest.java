package com.side.football_project.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.side.football_project.config.TestSecurityConfig;
import com.side.football_project.domain.user.dto.UserRequestDto;
import com.side.football_project.domain.user.dto.UserResponseDto;
import com.side.football_project.domain.user.service.UserService;
import com.side.football_project.domain.user.type.UserRole;
import com.side.football_project.domain.user.type.UserTier;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestSecurityConfig.class)
@WebMvcTest(AccountController.class)
class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void signUp() throws Exception {
        // Given
        UserRequestDto requestDto = UserRequestDto.builder()
                .email("testUser@email.com")
                .password("qwer!1234")
                .name("tester")
                .phoneNumber("01012345678")
                .age(30)
                .role(UserRole.NORMAL)
                .build();

        UserResponseDto responseDto = UserResponseDto.builder()
                .id(1L)
                .email("testUser@email.com")
                .name("tester")
                .phoneNumber("01012345678")
                .age(30)
                .userTier(UserTier.ROOKIE)
                .userRole(UserRole.NORMAL)
                .build();

        when(userService.createUser(any(UserRequestDto.class))).thenReturn(responseDto);

        // When & Then
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
    }

    @Test
    void login() throws Exception {
        // Given
        UserRequestDto requestDto = UserRequestDto.builder()
                .email("testUser@email.com")
                .password("qwer!1234")
                .build();

        doNothing().when(userService).login(any(UserRequestDto.class), any());

        // When & Then
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("로그인 성공"));
    }
}