package com.goldang.goldangtime.repository;

import com.goldang.goldangtime.entity.Matching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchingRepository extends JpaRepository<Matching, Long> {
    // 필요시 추가 쿼리 메서드 정의 가능
}
