package com.goldang.goldangtime.repository;

import com.goldang.goldangtime.entity.FoundPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoundPostRepository extends JpaRepository<FoundPost, Long> {
}


