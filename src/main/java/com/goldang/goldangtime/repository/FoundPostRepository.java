package com.goldang.goldangtime.repository;

import com.goldang.goldangtime.entity.LostPost;
import com.goldang.goldangtime.entity.Users;
import org.springframework.stereotype.Repository;
import com.goldang.goldangtime.entity.FoundPost;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface FoundPostRepository extends JpaRepository<FoundPost, Long> {
    List<FoundPost> findAllByUser(Users user);
    List<FoundPost> findByUserId(Long userId); // 사용자가 작성한 게시글 목록 조회
}
