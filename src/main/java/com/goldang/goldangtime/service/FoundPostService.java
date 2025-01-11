package com.goldang.goldangtime.service;

import com.goldang.goldangtime.entity.FoundPost;
import com.goldang.goldangtime.entity.LostPost;
import com.goldang.goldangtime.repository.LostPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
    public FoundPostDto createFoundPost(FoundPostDto foundPostDTO) throws IOException {
        FoundPost foundPost = convertToEntity(foundPostDTO);
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
    private FoundPost convertToEntity(FoundPostDto dto) {
        FoundPost foundPost = FoundPost.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .location(dto.getLocation())
                .foundPhoto(dto.getFoundPhoto())
                .scrap(dto.getScrap())
                .build();

        // User 설정
        if (dto.getUserId() != null) {
            Users user = userService.getUserById(dto.getUserId()); // UserService에서 사용자 조회
            foundPost.setUser(user);
        }
        return foundPost;
    }
}
