package com.goldang.goldangtime.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "scrap")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Scrap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "lost_post_id", nullable = true) // LostPost에 대한 스크랩
    private LostPost lostPost;

    @ManyToOne
    @JoinColumn(name = "found_post_id", nullable = true) // FoundPost에 대한 스크랩
    private FoundPost foundPost;
}
