package com.goldang.goldangtime.controller;

import com.goldang.goldangtime.dto.SignInDto;
import com.goldang.goldangtime.dto.SignUpDto;
import com.goldang.goldangtime.config.jwt.JwtToken;
import com.goldang.goldangtime.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody SignUpDto signUpDto){
        try {
            String result = userService.signUp(signUpDto.getEmail(), signUpDto.getPassword(), signUpDto.getNickname());
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/sign-in")
    public JwtToken signIn(@RequestBody SignInDto signInDto) {
        String email = signInDto.getEmail();
        String password = signInDto.getPassword();
        String fcmToken = signInDto.getFcmToken();
        log.info("Request email = {}, password = {}, fcmToken = {}", email, password, fcmToken);

        JwtToken jwtToken = userService.signIn(email, password, fcmToken);
        log.info("JwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());
        return jwtToken;
    }
}
