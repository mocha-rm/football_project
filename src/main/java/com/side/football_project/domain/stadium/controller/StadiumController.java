package com.side.football_project.domain.stadium.controller;

import com.side.football_project.domain.stadium.dto.StadiumRequestDto;
import com.side.football_project.domain.stadium.dto.StadiumResponseDto;
import com.side.football_project.domain.stadium.dto.StadiumUpdateDto;
import com.side.football_project.domain.stadium.service.StadiumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stadiums")
@RequiredArgsConstructor
public class StadiumController {
    private final StadiumService stadiumService;

    @PostMapping
    public ResponseEntity<StadiumResponseDto> createStadium(@RequestBody StadiumRequestDto requestDto) {
        return ResponseEntity.ok(stadiumService.createStadium(requestDto));
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<StadiumResponseDto> findStadium(@PathVariable Long id) {
        return ResponseEntity.ok(stadiumService.findStadium(id));
    }

    @PatchMapping
    public ResponseEntity<StadiumUpdateDto> updateStadium(
            @RequestBody StadiumUpdateDto requestDto) {
        return ResponseEntity.ok(stadiumService.updateStadium(requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStadium(@PathVariable Long id) {
        stadiumService.deleteStadium(id);
        return ResponseEntity.noContent().build();
    }
}
