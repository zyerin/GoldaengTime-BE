package com.goldang.goldangtime.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MyPageResponseDto {
    private Long id;           // 게시글 ID
    private String type;       // 게시글 유형 ("LostPost" 또는 "FoundPost")
    private String title;
    private String description;
    private String photo;
    private LocalDateTime createdAt;
}
