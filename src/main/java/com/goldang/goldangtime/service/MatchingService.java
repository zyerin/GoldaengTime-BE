package com.goldang.goldangtime.service;

import com.goldang.goldangtime.dto.LostPostDto;
import com.goldang.goldangtime.dto.FoundPostDto;
import com.goldang.goldangtime.entity.LostPost;
import com.goldang.goldangtime.entity.FoundPost;
import com.goldang.goldangtime.repository.LostPostRepository;
import com.goldang.goldangtime.repository.FoundPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchingService {

    private final LostPostRepository lostPostRepository;
    private final FoundPostRepository foundPostRepository;

    // 매칭된 발견된 게시글 목록 조회
    public List<FoundPostDto> matchLostPostWithFoundPosts(LostPostDto lostPostDTO) {
        LostPost lostPost = convertToEntity(lostPostDTO);

        // 매칭 조건 설정 (예: 제목, 위치, 사진 등)
        List<FoundPost> foundPosts = foundPostRepository.findAll().stream()
                .filter(foundPost -> matchCondition(lostPost, foundPost))
                .collect(Collectors.toList());

        return foundPosts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 매칭 조건을 정의 (예: 제목, 위치, 사진 등)
    private boolean matchCondition(LostPost lostPost, FoundPost foundPost) {
        // 간단한 예시로 제목과 위치가 같으면 매칭된 것으로 처리
        return lostPost.getTitle().equalsIgnoreCase(foundPost.getTitle()) &&
               lostPost.getLocation().equalsIgnoreCase(foundPost.getLocation());
    }

    // LostPostDto -> LostPosts 엔티티로 변환
    private LostPost convertToEntity(LostPostDto dto) {
        return LostPost.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .location(dto.getLocation())
                .lostPhoto(dto.getLostPhoto())
                .status(dto.getStatus())
                .scrap(dto.getScrap())
                .build();
    }

    // FoundPosts 엔티티 -> FoundPostDto 변환
    private FoundPostDto convertToDTO(FoundPost foundPost) {
        return FoundPostDto.builder()
                .id(foundPost.getId())
                .userId(foundPost.getUser().getId())
                .title(foundPost.getTitle())
                .description(foundPost.getDescription())
                .location(foundPost.getLocation())
                .foundPhoto(foundPost.getFoundPhoto())
                .scrap(foundPost.getScrap())
                .createdAt(foundPost.getCreatedAt())
                .build();
    }
}
