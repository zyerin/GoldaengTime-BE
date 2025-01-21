package com.goldang.goldangtime.service;

import com.goldang.goldangtime.dto.LostPostDto;
import com.goldang.goldangtime.entity.LostPost;
import com.goldang.goldangtime.entity.Pets;
import com.goldang.goldangtime.entity.Users;
import com.goldang.goldangtime.repository.LostPostRepository;
import com.goldang.goldangtime.repository.PetRepository;
import com.goldang.goldangtime.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LostPostService {

    private final UserRepository userRepository;
    private final PetRepository petRepository;
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
    public LostPostDto createLostPost(LostPostDto lostPostDto) {
        // 사용자 조회
        Users user = userRepository.findById(lostPostDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Pet 조회
        Pets pet = petRepository.findById(lostPostDto.getPetId())
                .orElseThrow(() -> new IllegalArgumentException("Pet not found"));

        LostPost lostPost = convertToEntity(lostPostDto, user, pet);    // 엔티티 변환
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

    private LostPost convertToEntity(LostPostDto dto, Users user, Pets pet) {
        return LostPost.builder()
                .user(user)
                .pet(pet)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .location(dto.getLocation())
                .status(dto.getStatus())
                .lostPhoto(dto.getLostPhoto())
                .createdAt(LocalDateTime.now()) // 현재 시간 설정
                .scrap(0) // 초기 스크랩 수는 0
                .build();
    }
}
