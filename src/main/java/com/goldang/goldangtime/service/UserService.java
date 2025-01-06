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

import java.util.ArrayList;
import java.util.Optional;

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
        Users users = new Users();
        users.setEmail(email);
        users.setPassword(passwordEncoder.encode(password));
        users.setNickname(nickname);

        userRepository.save(users);
        return "Sign Up Successfully";
    }

    @Transactional
    public JwtToken signIn(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        log.info("Passed signIn 1");

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        log.info("Passed signIn 2");

        // jwt 토큰 생성
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);
        log.info("Passed signIn 3");

        return jwtToken;
    }
}
