package com.goldang.goldangtime.controller;

import com.goldang.goldangtime.entity.FoundPost;
import com.goldang.goldangtime.service.FoundPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/found-posts")
@RequiredArgsConstructor
public class FoundPostController {

    private final FoundPostService foundPostService;

    /**
     * 발견 게시글의 이미지를 기반으로 실종 게시글과의 유사도 비교
     *
     * @param foundPost 발견 게시글 정보
     * @return 유사도가 높은 순으로 정렬된 실종 게시글 ID 리스트
     */
    @PostMapping("/compare")
    public ResponseEntity<List<Map<String, Object>>> compareFoundPost(@RequestBody FoundPost foundPost) throws IOException {
        List<Map<String, Object>> matchedPosts = foundPostService.compareWithLostPosts(foundPost);
        return ResponseEntity.ok(matchedPosts);
    }
}
