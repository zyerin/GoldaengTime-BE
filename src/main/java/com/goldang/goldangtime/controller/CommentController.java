package com.goldang.goldangtime.controller;

import com.goldang.goldangtime.dto.CommentRequestDto;
import com.goldang.goldangtime.dto.CommentResponseDto;
import com.goldang.goldangtime.entity.CustomUserDetails;
import com.goldang.goldangtime.service.CommentService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 또는 답글 달기
    @PostMapping("/add")
    @Operation(summary = "댓글 달기")
    public ResponseEntity<CommentResponseDto> addComment(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody CommentRequestDto requestDto) {
        String email = userDetails.getUsername();
        CommentResponseDto responseDto = commentService.addComment(email, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 댓글(답글) 수정
    @PutMapping("/{commentId}")
    @Operation(summary = "댓글 수정")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long commentId, @AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody CommentRequestDto requestDto) {
        String email = userDetails.getUsername();
        CommentResponseDto responseDto = commentService.updateComment(commentId, email, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 댓글(답글) 삭제
    @DeleteMapping("/{commentId}")
    @Operation(summary = "댓글 삭제")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        String email = userDetails.getUsername();
        String message = commentService.deleteComment(commentId, email);
        return ResponseEntity.ok(message);
    }

    // 게시글의 댓글(답글) 조회
    @GetMapping("/{postType}/{postId}")
    @Operation(summary = "댓글 조회")
    public ResponseEntity<List<CommentResponseDto>> getComments(@PathVariable String postType, @PathVariable Long postId) {
        List<CommentResponseDto> comments = commentService.getComments(postId, postType);
        return ResponseEntity.ok(comments);
    }
}

