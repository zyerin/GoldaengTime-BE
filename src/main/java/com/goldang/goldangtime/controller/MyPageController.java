package com.goldang.goldangtime.controller;

import com.goldang.goldangtime.dto.MyPageResponseDto;
import com.goldang.goldangtime.dto.ProfileRequestDto;
import com.goldang.goldangtime.entity.Comment;
import com.goldang.goldangtime.entity.Users;
import com.goldang.goldangtime.service.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {
    private final MyPageService myPageService;

    // 프로필 수정
    @PostMapping("/{userId}/profile")
    public ResponseEntity<Users> updateProfile(@PathVariable Long userId, @RequestBody ProfileRequestDto myPageDto) {
        String nickname = myPageDto.getNickname();
        String userPhoto = myPageDto.getUserPhoto();
        log.info("Update Profile for {}: ", nickname);

        Users updatedUser = myPageService.updateProfile(userId, nickname, userPhoto);
        return ResponseEntity.ok(updatedUser);
    }

    // 내가 쓴 글 조회 (LostPost, FoundPost 포함)
    @GetMapping("/{userId}/posts")
    public ResponseEntity<List<MyPageResponseDto>> getUserPosts(@PathVariable Long userId) {
        List<MyPageResponseDto> userPosts = myPageService.getUserPosts(userId);
        return ResponseEntity.ok(userPosts);
    }

    // 내가 쓴 댓글 조회
    @GetMapping("/{userId}/comments")
    public ResponseEntity<List<Comment>> getUserComments(@PathVariable Long userId) {
        List<Comment> userComments = myPageService.getUserComments(userId);
        return ResponseEntity.ok(userComments);
    }

    // 내가 스크랩한 글 조회
    @GetMapping("/{userId}/scraps")
    public ResponseEntity<List<MyPageResponseDto>> getUserScraps(@PathVariable Long userId) {
        List<MyPageResponseDto> scraps = myPageService.getUserScraps(userId);
        return ResponseEntity.ok(scraps);
    }
}
