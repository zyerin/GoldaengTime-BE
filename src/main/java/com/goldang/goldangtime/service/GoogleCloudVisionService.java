package com.goldang.goldangtime.service;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoogleCloudVisionService {

    private final ImageAnnotatorClient imageAnnotatorClient;

    // 이미지에서 특징 벡터 추출 (레이블, 색상, 객체 정보 포함)
    public List<Double> getImageFeatures(String imageName) throws IOException {
        // 리소스 폴더 내 이미지 파일 접근
        ClassPathResource resource = new ClassPathResource("images/" + imageName);
        ByteString imgBytes = ByteString.copyFrom(resource.getInputStream().readAllBytes());

        // Vision API 요청 생성
        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature labelFeature = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();
        Feature propertiesFeature = Feature.newBuilder().setType(Feature.Type.IMAGE_PROPERTIES).build();
        Feature objectFeature = Feature.newBuilder().setType(Feature.Type.OBJECT_LOCALIZATION).build();

        AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                .addFeatures(labelFeature)
                .addFeatures(propertiesFeature)
                .addFeatures(objectFeature)
                .setImage(img)
                .build();

        // Vision API 호출
        AnnotateImageResponse response = imageAnnotatorClient.batchAnnotateImages(List.of(request)).getResponses(0);

        if (response.hasError()) {
            throw new RuntimeException("Error while processing image: " + response.getError().getMessage());
        }

        // 수정 가능한 리스트 생성
        List<Double> features = new ArrayList<>();

        // 레이블 점수 추가
        features.addAll(extractLabelFeatures(response));

        // 색상 정보 추가
        features.addAll(extractColorFeatures(response));

        // 객체 크기 및 위치 정보 추가
        features.addAll(extractObjectFeatures(response));

        return features;
    }

    // 레이블 점수 추출
    private List<Double> extractLabelFeatures(AnnotateImageResponse response) {
        List<Double> labelFeatures = new ArrayList<>();
        response.getLabelAnnotationsList().forEach(annotation ->
                labelFeatures.add((double) annotation.getScore())); // float -> double 변환
        return labelFeatures;
    }

    // 색상 정보 추출
    private List<Double> extractColorFeatures(AnnotateImageResponse response) {
        List<Double> colorFeatures = new ArrayList<>();
        if (response.hasImagePropertiesAnnotation()) {
            DominantColorsAnnotation colors = response.getImagePropertiesAnnotation().getDominantColors();
            for (ColorInfo colorInfo : colors.getColorsList()) {
                colorFeatures.add((double) colorInfo.getColor().getRed());
                colorFeatures.add((double) colorInfo.getColor().getGreen());
                colorFeatures.add((double) colorInfo.getColor().getBlue());
                colorFeatures.add((double) colorInfo.getScore()); // 색상의 점수
            }
        }
        return colorFeatures;
    }

    // 객체 크기 및 위치 정보 추출
    private List<Double> extractObjectFeatures(AnnotateImageResponse response) {
        List<Double> objectFeatures = new ArrayList<>();
        for (LocalizedObjectAnnotation object : response.getLocalizedObjectAnnotationsList()) {
            objectFeatures.add((double) object.getScore()); // 객체 인식 점수
            objectFeatures.add(object.getBoundingPoly().getNormalizedVerticesList().stream()
                    .mapToDouble(vertex -> vertex.getX() + vertex.getY()).sum()); // 위치 정보
        }
        return objectFeatures;
    }
}
