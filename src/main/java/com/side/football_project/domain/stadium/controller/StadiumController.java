package com.side.football_project.domain.stadium.controller;

import com.side.football_project.domain.stadium.dto.StadiumRequestDto;
import com.side.football_project.domain.stadium.dto.StadiumResponseDto;
import com.side.football_project.domain.stadium.dto.StadiumUpdateDto;
import com.side.football_project.domain.stadium.service.StadiumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stadiums")
@RequiredArgsConstructor
public class StadiumController {
    private final StadiumService stadiumService;

    @PostMapping
    public ResponseEntity<StadiumResponseDto> createStadium(@RequestBody StadiumRequestDto requestDto) {
        return new ResponseEntity<>(stadiumService.createStadium(requestDto), HttpStatus.OK);
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<StadiumResponseDto> findStadium(@PathVariable Long id) {
        return new ResponseEntity<>(stadiumService.findStadium(id), HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<StadiumUpdateDto> updateStadium(
            @RequestBody StadiumUpdateDto requestDto) {
        return new ResponseEntity<>(stadiumService.updateStadium(requestDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStadium(@PathVariable Long id) {
        stadiumService.deleteStadium(id);
        return ResponseEntity.noContent().build();
    }
}
