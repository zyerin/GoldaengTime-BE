package com.goldang.goldangtime.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goldang.goldangtime.dto.FcmRequestDto;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FcmRequestService {
    private final ObjectMapper objectMapper;

    public String sendMessage(FcmRequestDto requestDto) throws JsonProcessingException, FirebaseMessagingException {
        // FCM 메시지 data 구성 -> json 변환
        String dataJson = objectMapper.writeValueAsString(requestDto.getData());
        log.info("Before: {}", requestDto.getData());
        log.info("FcmRequest Body: {}", dataJson);

        // FCM 메시지 생성
        FcmRequestDto.Data data = requestDto.getData();

        Message message = Message.builder()
                .setToken(requestDto.getFcmToken())
                .setNotification(Notification.builder()
                        .setTitle(requestDto.getNotification().getTitle())
                        .setBody(requestDto.getNotification().getBody())
                        .build())
                .putData("recordIdx", data.getFoundPostId())
                .build();
        log.info("Message: {}", message);

        // 메시지 전송
        String response = FirebaseMessaging.getInstance().send(message);
        log.info("Message sent successfully. FCM response: {}", response);

        return response;
    }

}

