package com.goldang.goldangtime.repository;

import com.goldang.goldangtime.entity.Comment;
import com.goldang.goldangtime.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByUser(Users user);

    // LostPost의 최상위 댓글 조회
    List<Comment> findAllByLostPostIdAndParentCommentIsNull(Long lostPostId);

    // FoundPost의 최상위 댓글 조회
    List<Comment> findAllByFoundPostIdAndParentCommentIsNull(Long foundPostId);
}

