package com.side.football_project.domain.shorts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.side.football_project.config.TestSecurityConfig;
import com.side.football_project.domain.shorts.dto.ShortsRequestDto;
import com.side.football_project.domain.shorts.dto.ShortsResponseDto;
import com.side.football_project.domain.shorts.service.ShortsService;
import com.side.football_project.global.common.service.S3Service;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestSecurityConfig.class)
@WebMvcTest(ShortsController.class)
class ShortsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @MockitoBean
    private ShortsService shortsService;

    @MockitoBean
    private S3Service s3Service;


    @Test
    void createShorts() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.mp4",
                "video/mp4", "video content".getBytes());

        String title = "title";
        String description = "description";
        String url = "https://s3.test.com/video.mp4";

        ShortsResponseDto responseDto = ShortsResponseDto.builder()
                .id(1L)
                .title(title)
                .description(description)
                .url(url).build();

        when(s3Service.uploadFile(any())).thenReturn(url);
        when(shortsService.createShorts(any())).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/shorts")
                        .file(file)
                        .param("title", title)
                        .param("description", description)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.description").value(description))
                .andExpect(jsonPath("$.url").value(url));
    }

    @Test
    void findShorts() throws Exception {
        ShortsResponseDto responseDto = ShortsResponseDto.builder()
                .id(1L)
                .title("title")
                .description("description")
                .url("https://s3.test.com/video.mp4").build();

        when(shortsService.findShorts(1L)).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/shorts/{shortsId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void findShortsFeed() throws Exception {
        List<ShortsResponseDto> shortsList = List.of(
                new ShortsResponseDto(
                        1L,
                        "title1",
                        "description1",
                        "http://s3.test.com/video1.mp4",
                        null),
                new ShortsResponseDto(
                        2L,
                        "title2",
                        "description2",
                        "http://s3.test.com/video2.mp4",
                        null)
        );

        Page<ShortsResponseDto> page = new PageImpl<>(
                shortsList,
                PageRequest.of(0, 10),
                shortsList.size());

        when(shortsService.findShortsFeed(0, 10)).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/shorts/feed")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    void updateShorts() throws Exception {
        ShortsRequestDto requestDto = ShortsRequestDto.builder()
                .title("updated title")
                .description("updated description")
                .url("https://s3.test.com/video.mp4")
                .build();

        ShortsResponseDto responseDto = ShortsResponseDto.builder()
                .id(1L)
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .url(requestDto.getUrl())
                .build();

        when(shortsService.updateShorts(any(), any())).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.patch("/shorts/{shortsId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("updated title"))
                .andExpect(jsonPath("$.description").value("updated description"));
    }

    @Test
    void deleteShorts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/shorts/{shortsId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}