package com.goldang.goldangtime.service;

import com.goldang.goldangtime.entity.FoundPost;
import com.goldang.goldangtime.entity.LostPost;
import com.goldang.goldangtime.repository.LostPostRepository;
import com.goldang.goldangtime.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import com.goldang.goldangtime.dto.FoundPostDto;
import com.goldang.goldangtime.entity.Users;
import com.goldang.goldangtime.repository.FoundPostRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoundPostService {

    private final FoundPostRepository foundPostRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final MatchingService matchingService;

    public List<FoundPostDto> getAllFoundPosts() {
        return foundPostRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 특정 발견된 게시글 조회
    public FoundPostDto getFoundPostById(Long id) {
        FoundPost foundPost = foundPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("FoundPost not found with id: " + id));
        return convertToDTO(foundPost);
    }

    // 발견된 게시글 생성
    @Transactional
    public FoundPostDto createFoundPost(FoundPostDto foundPostDto) throws IOException {
        // 사용자 조회
        Users user = userRepository.findById(foundPostDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        FoundPost foundPost = convertToEntity(foundPostDto, user);
        FoundPost savedPost = foundPostRepository.save(foundPost);

        // 발견된 게시글 생성 후 유사도 비교 및 매칭 저장
        matchingService.compareAndSaveMatches(savedPost);

        return convertToDTO(savedPost);
    }

    // 엔티티를 DTO로 변환
    private FoundPostDto convertToDTO(FoundPost foundPost) {
        return FoundPostDto.builder()
                .id(foundPost.getId())
                .userId(foundPost.getUser() != null ? foundPost.getUser().getId() : null) // User가 null일 경우 처리
                .title(foundPost.getTitle())
                .description(foundPost.getDescription())
                .location(foundPost.getLocation())
                .foundPhoto(foundPost.getFoundPhoto())
                .scrap(foundPost.getScrap())
                .createdAt(foundPost.getCreatedAt())
                .build();
    }

    // DTO를 엔티티로 변환
    private FoundPost convertToEntity(FoundPostDto dto, Users user) {
        return FoundPost.builder()
                .user(user)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .foundPhoto(dto.getFoundPhoto())
                .location(dto.getLocation())
                .createdAt(LocalDateTime.now()) // 현재 시간 설정
                .scrap(0) // 초기 스크랩 수는 0
                .build();
    }
}
