package com.goldang.goldangtime.controller;

import com.goldang.goldangtime.dto.FoundPostDto;
import com.goldang.goldangtime.dto.LostPostDto;
import com.goldang.goldangtime.service.MatchingService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/matching")
@RequiredArgsConstructor
public class MatchingController {

    private final MatchingService matchingService;

    
    @PostMapping("/find")
    @Operation(summary ="LostPost와 매칭된 FoundPost 조회")
    public ResponseEntity<List<FoundPostDto>> findMatchingFoundPosts(@RequestBody LostPostDto lostPostDto) {
        List<FoundPostDto> matchedFoundPosts = matchingService.matchLostPostWithFoundPosts(lostPostDto);
        return ResponseEntity.ok(matchedFoundPosts);
    }
}
