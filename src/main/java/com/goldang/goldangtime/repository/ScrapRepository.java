package com.goldang.goldangtime.repository;

import com.goldang.goldangtime.entity.Scrap;
import com.goldang.goldangtime.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    boolean existsByUserIdAndLostPostId(Long userId, Long lostPostId);
    boolean existsByUserIdAndFoundPostId(Long userId, Long foundPostId);
    List<Scrap> findAllByUser(Users user);
}
