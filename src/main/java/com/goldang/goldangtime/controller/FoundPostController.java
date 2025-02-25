package com.goldang.goldangtime.controller;


import com.goldang.goldangtime.entity.FoundPost;
import com.goldang.goldangtime.dto.FoundPostDto;
import com.goldang.goldangtime.service.FoundPostService;

import com.goldang.goldangtime.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/found-posts")
@RequiredArgsConstructor
public class FoundPostController {

    private final MatchingService matchingService;
    private final FoundPostService foundPostService;

    @GetMapping
    @Operation(summary  = "모든 FoundPost 조회")
    public List<FoundPostDto> getAllFoundPosts(){
        return foundPostService.getAllFoundPosts();
    }

    @PostMapping
    @Operation(summary = "FoundPost 생성")
    public ResponseEntity<?> createdFoundPost(@RequestBody FoundPostDto foundPostDTO) {
        try {
            FoundPostDto createdPost = foundPostService.createFoundPost(foundPostDTO);
            return ResponseEntity.ok(createdPost);
        } catch (IOException e) {
            // 오류 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create FoundPost: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "특정 아이디로 게시글 조회")
    public ResponseEntity<FoundPostDto> getFoundPostById(@PathVariable Long id) {
        FoundPostDto foundPost = foundPostService.getFoundPostById(id);
        return ResponseEntity.ok(foundPost);
    }
}
