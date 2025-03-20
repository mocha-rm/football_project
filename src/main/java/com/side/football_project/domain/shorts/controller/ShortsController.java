package com.side.football_project.domain.shorts.controller;

import com.side.football_project.domain.shorts.dto.ShortsRequestDto;
import com.side.football_project.domain.shorts.dto.ShortsResponseDto;
import com.side.football_project.domain.shorts.service.ShortsService;
import com.side.football_project.global.common.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shorts")
public class ShortsController {
    private final ShortsService shortsService;
    private final S3Service s3Service;

    @PostMapping
    public ResponseEntity<ShortsResponseDto> createShorts(@RequestParam("file")MultipartFile file,
                                                          @RequestParam("title") String title,
                                                          @RequestParam("description") String description) {
        String url = s3Service.uploadFile(file);
        ShortsRequestDto requestDto = new ShortsRequestDto(title, description, url);
        return ResponseEntity.ok(shortsService.createShorts(requestDto));
    }

    @GetMapping("/{shortsId}")
    public ResponseEntity<ShortsResponseDto> findShorts(@PathVariable Long shortsId) {
        return ResponseEntity.ok(shortsService.findShorts(shortsId));
    }

    @PatchMapping("/{shortsId}")
    public ResponseEntity<ShortsResponseDto> updateShorts(@PathVariable Long shortsId,
                                                          @RequestBody ShortsRequestDto requestDto) {
        return ResponseEntity.ok(shortsService.updateShorts(shortsId, requestDto));
    }

    @DeleteMapping("/{shortsId}")
    public ResponseEntity<Void> deleteShorts(@PathVariable Long shortsId) {
        shortsService.deleteShorts(shortsId);
        return ResponseEntity.noContent().build();
    }
}