package com.goldang.goldangtime.repository;

import com.goldang.goldangtime.entity.LostPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LostPostRepository extends JpaRepository<LostPost, Long> {
}

