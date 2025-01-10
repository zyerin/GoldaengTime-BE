package com.goldang.goldangtime.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import com.goldang.goldangtime.dto.FoundPostDto;
import com.goldang.goldangtime.service.FoundPostService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/found-posts")
@RequiredArgsConstructor
public class FoundPostController {
    private final FoundPostService foundPostService;
    
    @GetMapping
    @Operation(summary  = "모든 FoundPost 조회")
    public List<FoundPostDto> getAllFoundPosts(){
        return foundPostService.getAllFoundPosts();
    }
    
    @PostMapping
    @Operation(summary =  "FoundPost 생성")
    public FoundPostDto createdFoundPost(@RequestBody FoundPostDto foundPostDTO) {
        return foundPostService.createFoundPost(foundPostDTO);
    }

    
    @GetMapping("/{id}")
    @Operation(summary = "특정 아이디로 게시글 조회")
    public ResponseEntity<FoundPostDto> getFoundPostById(@PathVariable Long id) {
        FoundPostDto foundPost = foundPostService.getFoundPostById(id);
        return ResponseEntity.ok(foundPost);
    }
}
