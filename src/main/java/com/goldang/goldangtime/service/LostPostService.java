package com.goldang.goldangtime.service;

import com.goldang.goldangtime.dto.LostPostDto;
import com.goldang.goldangtime.entity.LostPost;
import com.goldang.goldangtime.entity.Pets;
import com.goldang.goldangtime.entity.Users;
import com.goldang.goldangtime.repository.LostPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LostPostService {

    private final LostPostRepository lostPostRepository;
    private final UserService userService; // UserService 의존성 추가
   
    // 모든 게시글 조회
    public List<LostPostDto> getAllLostPosts() {
        return lostPostRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 특정 게시글 조회
    public LostPostDto getLostPostById(Long id) {
        LostPost lostPost = lostPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("LostPost not found with id: " + id));
        return convertToDTO(lostPost);
    }

    // 게시글 생성
    @Transactional
    public LostPostDto createLostPost(LostPostDto lostPostDTO) {
        LostPost lostPost = convertToEntity(lostPostDTO);
        return convertToDTO(lostPostRepository.save(lostPost));
    }

    // 엔티티를 DTO로 변환
    private LostPostDto convertToDTO(LostPost lostPost) {
        return LostPostDto.builder()
                .id(lostPost.getId())
                .userId(lostPost.getUser() != null ? lostPost.getUser().getId() : null) // User가 null일 경우 처리
                .petId(lostPost.getPet() != null ? lostPost.getPet().getId() : null)   // Pet이 null일 경우 처리
                .title(lostPost.getTitle())
                .description(lostPost.getDescription())
                .location(lostPost.getLocation())
                .lostPhoto(lostPost.getLostPhoto())
                .status(lostPost.getStatus())
                .scrap(lostPost.getScrap())
                .createdAt(lostPost.getCreatedAt())
                .build();
    }

    // DTO를 엔티티로 변환
    private LostPost convertToEntity(LostPostDto dto) {
        LostPost lostPost = LostPost.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .location(dto.getLocation())
                .lostPhoto(dto.getLostPhoto())
                .status(dto.getStatus())
                .scrap(dto.getScrap())
                .build();

        // User 설정
        if (dto.getUserId() != null) {
            Users user = userService.getUserById(dto.getUserId()); // UserService에서 사용자 조회
            lostPost.setUser(user);
        }

        return lostPost;
    }
}
