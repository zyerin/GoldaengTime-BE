package com.goldang.goldangtime.repository;

import com.goldang.goldangtime.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);

    Optional<Users> findByNickname(String nickname);

    Optional<Users> findByFcmToken(String fcmToken);

    // 이메일 존재 여부
    boolean existsByEmail(String email);

    // 닉네임 존재 여부
    boolean existsByNickname(String nickname);
}
