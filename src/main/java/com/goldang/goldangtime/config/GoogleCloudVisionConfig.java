package com.goldang.goldangtime.config;

import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageAnnotatorSettings;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class GoogleCloudVisionConfig {

    @Bean
    public ImageAnnotatorClient imageAnnotatorClient() {
        try {
            // ClassPath 경로로 참조
            GoogleCredentials credentials = GoogleCredentials.fromStream(
                    new ClassPathResource("firebase/goldaengtime-firebase-adminsdk.json").getInputStream()
            );

            // 상대경로 지정
//            GoogleCredentials credentials = GoogleCredentials.fromStream(
//                    new FileInputStream("/app/firebase/goldaengtime-firebase-adminsdk.json.json")
//            );

            ImageAnnotatorSettings settings = ImageAnnotatorSettings.newBuilder()
                    .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                    .build();

            return ImageAnnotatorClient.create(settings);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create ImageAnnotatorClient", e);
        }
    }
}
