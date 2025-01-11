package com.goldang.goldangtime.service;

import com.goldang.goldangtime.dto.MyPageResponseDto;
import com.goldang.goldangtime.entity.Comment;
import com.goldang.goldangtime.entity.LostPost;
import com.goldang.goldangtime.entity.Scrap;
import com.goldang.goldangtime.entity.Users;
import com.goldang.goldangtime.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final LostPostRepository lostPostRepository;
    private final FoundPostRepository foundPostRepository;
    private final ScrapRepository scrapRepository;

    // 프로필 수정
    @Transactional
    public Users updateProfile(Long userId, String nickname, String userPhoto) {
        // 사용자 조회
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        // 닉네임과 프로필 사진 수정
        user.setNickname(nickname);
        user.setUserPhoto(userPhoto);

        return userRepository.save(user);
    }

    // 사용자가 작성한 글 조회 -> MyPageResponseDto 반환
    public List<MyPageResponseDto> getUserPosts(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 사용자가 작성한 LostPost 조회 및 변환
        List<MyPageResponseDto> lostPosts = lostPostRepository.findAllByUser(user).stream()
                .map(this::convertLostPostToDto)
                .collect(Collectors.toList());

        // 사용자가 작성한 FoundPost 조회 및 변환
        List<MyPageResponseDto> foundPosts = foundPostRepository.findAllByUser(user).stream()
                .map(this::convertFoundPostToDto)
                .collect(Collectors.toList());

        // LostPost와 FoundPost 리스트 합치기
        lostPosts.addAll(foundPosts);

        return lostPosts;
    }


    // 사용자가 쓴 댓글 조회
    public List<Comment> getUserComments(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return commentRepository.findAllByUser(user);
    }

    // 사용자가 스크랩한 게시글 조회 -> MyPageResponseDto 반환
    public List<MyPageResponseDto> getUserScraps(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        List<Scrap> scraps = scrapRepository.findAllByUser(user);

        return scraps.stream()
                .map(scrap -> {
                    if (scrap.getLostPost() != null) {
                        return convertLostPostToDto(scrap.getLostPost());
                    } else {
                        return convertFoundPostToDto(scrap.getFoundPost());
                    }
                })
                .collect(Collectors.toList());
    }

    // LostPost를 MyPageResponseDto로 변환
    private MyPageResponseDto convertLostPostToDto(LostPost lostPost) {
        return MyPageResponseDto.builder()
                .id(lostPost.getId())
                .type("LostPost")
                .title(lostPost.getTitle())
                .description(lostPost.getDescription())
                .photo(lostPost.getLostPhoto())
                .createdAt(lostPost.getCreatedAt())
                .build();
    }

    // FoundPost를 MyPageResponseDto로 변환
    private MyPageResponseDto convertFoundPostToDto(com.goldang.goldangtime.entity.FoundPost foundPost) {
        return MyPageResponseDto.builder()
                .id(foundPost.getId())
                .type("FoundPost")
                .title(foundPost.getTitle())
                .description(foundPost.getDescription())
                .photo(foundPost.getFoundPhoto())
                .createdAt(foundPost.getCreatedAt())
                .build();
    }
}
