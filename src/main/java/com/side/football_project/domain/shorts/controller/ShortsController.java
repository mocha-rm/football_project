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

    /**
     * 숏츠 생성
     * @param file 숏츠 파일
     * @param title 숏츠 제목
     * @param description 숏츠 설명
     * @return 생성된 숏츠의 정보 {@link ShortsResponseDto}
     */
    @PostMapping
    public ResponseEntity<ShortsResponseDto> createShorts(@RequestParam("file") MultipartFile file,
                                                          @RequestParam("title") String title,
                                                          @RequestParam("description") String description) {
        String url = s3Service.uploadFile(file);
        ShortsRequestDto requestDto = new ShortsRequestDto(title, description, url);
        return ResponseEntity.ok(shortsService.createShorts(requestDto));
    }

    /**
     * 숏츠 조회
     * @param shortsId 숏츠 ID
     * @return 숏츠 정보
     */
    @GetMapping("/{shortsId}")
    public ResponseEntity<ShortsResponseDto> findShorts(@PathVariable Long shortsId) {
        return ResponseEntity.ok(shortsService.findShorts(shortsId));
    }

    /**
     * 숏츠 수정
     * @param shortsId 숏츠 ID
     * @param requestDto 숏츠 수정에 필요한 정보 {@link ShortsRequestDto}
     * @return 수정된 숏츠의 정보
     */
    @PatchMapping("/{shortsId}")
    public ResponseEntity<ShortsResponseDto> updateShorts(@PathVariable Long shortsId,
                                                          @RequestBody ShortsRequestDto requestDto) {
        return ResponseEntity.ok(shortsService.updateShorts(shortsId, requestDto));
    }

    /**
     * 숏츠 삭제
     * @param shortsId 숏츠 ID
     */
    @DeleteMapping("/{shortsId}")
    public ResponseEntity<Void> deleteShorts(@PathVariable Long shortsId) {
        shortsService.deleteShorts(shortsId);
        return ResponseEntity.noContent().build();
    }
}