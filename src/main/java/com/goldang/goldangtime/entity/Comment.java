package com.goldang.goldangtime.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // LostPost와 연관 관계 설정
    @ManyToOne
    @JoinColumn(name = "lost_post_id")
    private LostPost lostPost;

    // FoundPost와 연관 관계 설정
    @ManyToOne
    @JoinColumn(name = "found_post_id")
    private FoundPost foundPost;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    @Column(nullable = false)
    private Boolean secret;

    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;  // 처음 댓글을 달 때만 생성되고 이후로는 수정 x

    @UpdateTimestamp
    @Column(name = "modified_date", updatable = false)
    private LocalDateTime modifiedDate;

    // 부모 댓글
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parentComment;

    // 자식 댓글(답글)
    @OneToMany(mappedBy = "parentComment", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> replies = new ArrayList<>();
}

