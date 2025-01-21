package com.goldang.goldangtime.service;

import com.goldang.goldangtime.config.jwt.JwtToken;
import com.goldang.goldangtime.config.jwt.JwtTokenProvider;
import com.goldang.goldangtime.entity.Users;
import com.goldang.goldangtime.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String signUp(String email, String password, String nickname){
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("User already exist.");
        }
        Users user = new Users();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setNickname(nickname);

        userRepository.save(user);
        return "Sign Up Successfully";
    }

    @Transactional
    public JwtToken signIn(String email, String password, String fcmToken) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        log.info("Passed signIn 1");

        // AuthenticationManager에서 CustomUserDetailsService의 loadUserByUsername을 호출
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        log.info("Passed signIn 2");

        // 인증된 사용자 조회
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
        // fcmToken 업데이트
        user.setFcmToken(fcmToken);
        log.info("Updated fcmToken for user: {}", email);

        // jwt 토큰 생성
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);
        log.info("Passed signIn 3");

        return jwtToken;
    }

    // 특정 사용자 조회
    public Users getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
    }
}
