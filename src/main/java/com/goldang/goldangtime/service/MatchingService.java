package com.goldang.goldangtime.service;

import com.goldang.goldangtime.dto.LostPostDto;
import com.goldang.goldangtime.dto.FoundPostDto;
import com.goldang.goldangtime.entity.LostPost;
import com.goldang.goldangtime.entity.FoundPost;
import com.goldang.goldangtime.repository.LostPostRepository;
import com.goldang.goldangtime.repository.FoundPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchingService {

    private final GoogleCloudVisionService visionService;
    private final LostPostRepository lostPostRepository;
    private final FoundPostRepository foundPostRepository;

    /**
     * 발견 게시글의 이미지를 기반으로 유사도가 높은 실종 게시글 ID와 유사도 리스트 반환
     *
     * @param foundPost 발견 게시글
     * @return 유사도가 높은 순으로 정렬된 실종 게시글 ID와 유사도 리스트
     */
    public List<Map<String, Object>> compareWithLostPosts(FoundPost foundPost) throws IOException {
        List<Double> foundFeatures = visionService.getImageFeatures(foundPost.getFoundPhoto());

        // 모든 실종 게시글과의 유사도 계산
        List<LostPost> lostPosts = lostPostRepository.findAll();
        List<Map<String, Object>> results = new ArrayList<>();

        for (LostPost lostPost : lostPosts) {
            List<Double> lostFeatures = visionService.getImageFeatures(lostPost.getLostPhoto());

            // 두 벡터 크기를 맞춤
            int maxSize = Math.max(foundFeatures.size(), lostFeatures.size());
            List<Double> normalizedFoundFeatures = normalizeVectorSize(foundFeatures, maxSize);
            List<Double> normalizedLostFeatures = normalizeVectorSize(lostFeatures, maxSize);

            // 유사도 계산
            double similarity = calculateCosineSimilarity(normalizedFoundFeatures, normalizedLostFeatures);

            // 결과 추가
            Map<String, Object> result = new HashMap<>();
            result.put("id", lostPost.getId());
            result.put("similarity", similarity);
            results.add(result);
        }

        // 유사도 점수로 정렬
        return results.stream()
                .sorted((a, b) -> Double.compare((double) b.get("similarity"), (double) a.get("similarity")))
                .collect(Collectors.toList());
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
