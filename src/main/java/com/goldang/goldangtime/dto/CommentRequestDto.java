package com.goldang.goldangtime.dto;

import com.goldang.goldangtime.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddCommentRequest {
    private Long id;
    private String text;
    private String createdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    private String modifiedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    private String user;
    private int lostPost;
    private int foundPost;

    public Comment toEntity() {//생성자를 사용해 객체 생성
        return Comment.builder()
                .text(text)
                .createdDate(createdDate)
                .modifiedDate(modifiedDate)
                .user(user)
                .lostPost(lostPost)
                .foundPost(foundPost)
                .build();
    }
}
