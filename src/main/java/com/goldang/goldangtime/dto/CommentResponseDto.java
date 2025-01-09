package com.goldang.goldangtime.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class CommentResponseDto {
    private Long id;
    private String username; // 댓글 작성자
    private String text; // 댓글 내용
    private Boolean secret; // 비밀 댓글 여부
    private LocalDateTime createdDate; // 작성 시간
    private LocalDateTime modifiedDate; // 수정 시간
    private List<CommentResponseDto> replies; // 답글 리스트
}

