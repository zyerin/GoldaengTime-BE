package com.goldang.goldangtime.controller;

import com.goldang.goldangtime.dto.LostPostDto;
import com.goldang.goldangtime.service.LostPostService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/lost-posts")
@RequiredArgsConstructor
public class LostPostController {

    private final LostPostService lostPostService;

    @GetMapping
    @Operation(summary = "모든 LostPost 조회")
    public ResponseEntity<List<LostPostDto>> getAllLostPosts() {
        List<LostPostDto> lostPosts = lostPostService.getAllLostPosts();
        return ResponseEntity.ok(lostPosts);
    }

     
    @PostMapping
    @Operation(summary = "LostPost 생성")
    public ResponseEntity<LostPostDto> createLostPost(@RequestBody LostPostDto lostPostDto) {
        LostPostDto createdPost = lostPostService.createLostPost(lostPostDto);
        return ResponseEntity.ok(createdPost);
    }

    @GetMapping("/{id}")
    @Operation(summary = "특정 LostPost 조회")
    public ResponseEntity<LostPostDto> getLostPostById(@PathVariable Long id) {
        LostPostDto lostPost = lostPostService.getLostPostById(id);
        return ResponseEntity.ok(lostPost);
    }
}
