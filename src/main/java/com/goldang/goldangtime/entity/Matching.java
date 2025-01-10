package com.goldang.goldangtime.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;  // Set을 임포트

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
public class Matching {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(
        name = "matching_lostpost", // 중간 테이블 이름
        joinColumns = @JoinColumn(name = "matching_id"), // matching 엔티티와의 관계
        inverseJoinColumns = @JoinColumn(name = "lostpost_id") // LostPost 엔티티와의 관계
    )
    private Set<LostPost> lostPosts; // Set으로 수정 (다대다 관계이므로 Set이 적합)

    @ManyToOne
    @JoinColumn(name = "foundpost_id")
    private FoundPost foundPost;

    private Double similarity;
    private Boolean status;
}
