package com.goldang.goldangtime.service;

import com.goldang.goldangtime.entity.*;
import com.goldang.goldangtime.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScrapService {

    private final ScrapRepository scrapRepository;
    private final LostPostRepository lostPostRepository;
    private final FoundPostRepository foundPostRepository;

    @Transactional
    public void scrapLostPost(Long userId, Long lostPostId) {
        // 스크랩 중복 확인
        if (scrapRepository.existsByUserIdAndLostPostId(userId, lostPostId)) {
            throw new IllegalArgumentException("이미 스크랩한 게시글입니다.");
        }

        // 사용자와 게시글 조회
        LostPost lostPost = lostPostRepository.findById(lostPostId)
                .orElseThrow(() -> new IllegalArgumentException("해당 실종 게시글을 찾을 수 없습니다."));
        Users user = lostPost.getUser();

        // Scrap 생성
        Scrap scrap = createScrap(user, lostPost, null);
        scrapRepository.save(scrap);

        // 게시글 스크랩 수 증가
        lostPost.setScrap(lostPost.getScrap() + 1);
        lostPostRepository.save(lostPost);
    }

    @Transactional
    public void scrapFoundPost(Long userId, Long foundPostId) {
        // 스크랩 중복 확인
        if (scrapRepository.existsByUserIdAndFoundPostId(userId, foundPostId)) {
            throw new IllegalArgumentException("이미 스크랩한 게시글입니다.");
        }

        // 사용자와 게시글 조회
        FoundPost foundPost = foundPostRepository.findById(foundPostId)
                .orElseThrow(() -> new IllegalArgumentException("해당 발견 게시글을 찾을 수 없습니다."));
        Users user = foundPost.getUser();

        // Scrap 생성
        Scrap scrap = createScrap(user, null, foundPost);
        scrapRepository.save(scrap);

        // 게시글 스크랩 수 증가
        foundPost.setScrap(foundPost.getScrap() + 1);
        foundPostRepository.save(foundPost);
    }

    // scrap 엔티티 생성
    private Scrap createScrap(Users user, LostPost lostPost, FoundPost foundPost) {
        return Scrap.builder()
                .user(user)
                .lostPost(lostPost) // 실종 게시글
                .foundPost(foundPost) // 발견 게시글
                .build();
    }
}
