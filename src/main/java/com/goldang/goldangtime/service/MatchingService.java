package com.goldang.goldangtime.service;

import com.goldang.goldangtime.dto.FcmRequestDto;
import com.goldang.goldangtime.dto.LostPostDto;
import com.goldang.goldangtime.dto.FoundPostDto;
import com.goldang.goldangtime.entity.LostPost;
import com.goldang.goldangtime.entity.FoundPost;
import com.goldang.goldangtime.entity.Matching;
import com.goldang.goldangtime.repository.LostPostRepository;
import com.goldang.goldangtime.repository.FoundPostRepository;
import com.goldang.goldangtime.repository.MatchingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchingService {

    private final GoogleCloudVisionService visionService;
    private final LostPostRepository lostPostRepository;
    private final FoundPostRepository foundPostRepository;
    private final MatchingRepository matchingRepository;
    private final FcmRequestService fcmRequestService;


    /**
     * 발견 게시글의 이미지를 기반으로 유사도가 높은 실종 게시글 ID와 유사도를 계산 후 Matching 엔티티에 저장.
     *
     * @param foundPost 발견 게시글
     * @return 저장된 Matching 엔티티 리스트
     */
    public List<Matching> compareAndSaveMatches(FoundPost foundPost) throws IOException {
        List<Double> foundFeatures = visionService.getImageFeatures(foundPost.getFoundPhoto());

        // 모든 실종 게시글과의 유사도 계산
        List<LostPost> lostPosts = lostPostRepository.findAll();
        List<Matching> savedMatches = new ArrayList<>();

        for (LostPost lostPost : lostPosts) {
            List<Double> lostFeatures = visionService.getImageFeatures(lostPost.getLostPhoto());

            // 두 벡터 크기를 맞춤
            int maxSize = Math.max(foundFeatures.size(), lostFeatures.size());
            List<Double> normalizedFoundFeatures = normalizeVectorSize(foundFeatures, maxSize);
            List<Double> normalizedLostFeatures = normalizeVectorSize(lostFeatures, maxSize);

            // 유사도 계산
            double similarity = calculateCosineSimilarity(normalizedFoundFeatures, normalizedLostFeatures);

            // Matching 엔티티 생성 및 저장
            Matching matching = Matching.builder()
                    .foundPost(foundPost)
                    .lostPosts(Set.of(lostPost)) // LostPost는 Set으로 관리
                    .similarity(similarity)
                    .status(similarity >= 0.85) // 유사도가 0.85 이상인 경우 true
                    .build();

            matchingRepository.save(matching);
            savedMatches.add(matching);

            // 유사도가 0.85 이상인 경우(status가 true인 경우) FCM 알림 전송
            if (matching.getStatus()) {
                sendFcmNotification(lostPost, foundPost);
            }
        }

        return savedMatches;
    }

    /**
     * 두 벡터 간의 코사인 유사도 계산
     *
     * @param vectorA 첫 번째 벡터
     * @param vectorB 두 번째 벡터
     * @return 코사인 유사도 점수
     */
    private double calculateCosineSimilarity(List<Double> vectorA, List<Double> vectorB) {
        if (vectorA.size() != vectorB.size()) {
            throw new IllegalArgumentException("The vectors have different sizes: vectorA=" + vectorA.size() + ", vectorB=" + vectorB.size());
        }

        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (int i = 0; i < vectorA.size(); i++) {
            dotProduct += vectorA.get(i) * vectorB.get(i);
            normA += Math.pow(vectorA.get(i), 2);
            normB += Math.pow(vectorB.get(i), 2);
        }

        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    private List<Double> normalizeVectorSize(List<Double> vector, int targetSize) {
        List<Double> normalizedVector = new ArrayList<>(vector);
        while (normalizedVector.size() < targetSize) {
            normalizedVector.add(0.0); // 부족한 값을 0.0으로 채움
        }
        return normalizedVector;
    }

    // 푸시알림 전송 메서드 -> status가 true인 실종 게시글 작성자에게
    private void sendFcmNotification(LostPost lostPost, FoundPost foundPost) {
        try {
            // LostPost 작성자의 FCM 토큰 가져오기
            String fcmToken = lostPost.getUser().getFcmToken();
            log.info("{}'s FCM Token: {}", lostPost.getUser().getNickname(), fcmToken);

            // 알림 내용 작성
            FcmRequestDto.Notification notification = FcmRequestDto.Notification.builder()
                    .title("GoldaengTime")
                    .body("귀하의 실종 게시글과 유사한 발견 게시글이 등록되었습니다: " + foundPost.getTitle())
                    .build();

            // 데이터 작성
            FcmRequestDto.Data data = FcmRequestDto.Data.builder()
                    .foundPostId(String.valueOf(foundPost.getId()))
                    .build();

            // FCM 요청 생성
            FcmRequestDto requestDto = FcmRequestDto.builder()
                    .fcmToken(fcmToken)
                    .notification(notification)
                    .data(data)
                    .build();

            // FCM 메시지 전송
            fcmRequestService.sendMessage(requestDto);
        } catch (Exception e) {
            log.error("Failed to send FCM notification for LostPost ID {}: {}", lostPost.getId(), e.getMessage());
        }
    }

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
