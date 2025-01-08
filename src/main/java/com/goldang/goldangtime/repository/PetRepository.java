package com.goldang.goldangtime.repository;

import com.goldang.goldangtime.entity.Pets;
import com.goldang.goldangtime.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<Pets, Long> {

    // 사용자의 모든 반려동물 조회
    List<Pets> findByUser(Users user);

    // 사용자와 반려동물 이름으로 조회
    Optional<Pets> findByUserAndPetName(Users user, String petName);
}

