package com.goldang.goldangtime.controller;

import com.goldang.goldangtime.service.ScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/scrap")
@RequiredArgsConstructor
public class ScrapController {

    private final ScrapService scrapService;

    @PostMapping("/lost-post/{lostPostId}")
    public ResponseEntity<String> scrapLostPost(@RequestParam Long userId, @PathVariable Long lostPostId) {
        scrapService.scrapLostPost(userId, lostPostId);
        return ResponseEntity.ok(String.format("Scrap lost post successfully: %d", lostPostId));
    }

    @PostMapping("/found-post/{foundPostId}")
    public ResponseEntity<String> scrapFoundPost(@RequestParam Long userId, @PathVariable Long foundPostId) {
        scrapService.scrapFoundPost(userId, foundPostId);
        return ResponseEntity.ok(String.format("Scrap found post successfully: %d", foundPostId));
    }
}
