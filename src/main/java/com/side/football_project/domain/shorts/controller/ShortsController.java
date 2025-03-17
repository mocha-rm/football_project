package com.side.football_project.domain.shorts.controller;

import com.side.football_project.domain.shorts.dto.ShortsRequestDto;
import com.side.football_project.domain.shorts.dto.ShortsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shorts")
public class ShortsController {

    @PostMapping
    public ResponseEntity<ShortsResponseDto> createShorts(@RequestBody ShortsRequestDto requestDto) {
        return null;
        //return ResponseEntity.ok()
    }
}
