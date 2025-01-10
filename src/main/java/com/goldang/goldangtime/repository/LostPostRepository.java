package com.goldang.goldangtime.repository;

import com.goldang.goldangtime.entity.LostPost;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface LostPostRepository extends JpaRepository<LostPost, Long> {
    List<LostPost> findByUserId(Long userId); // 사용자가 작성한 게시글 목록 조회
}

