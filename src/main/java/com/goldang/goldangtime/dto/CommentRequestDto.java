package com.goldang.goldangtime.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {
    private Long postId; // LostPost or FoundPost ID
    private String text; // 댓글 내용
    private Boolean secret; // 비밀 댓글 여부
    private String postType; // lost 또는 found
    private Long parentCommentId;  // 부모 댓글 ID(해당 댓글이 답글인 경우 설정)
}

