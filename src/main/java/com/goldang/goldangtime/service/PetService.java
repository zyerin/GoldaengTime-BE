package com.goldang.goldangtime.service;

import com.goldang.goldangtime.dto.PetRequestDto;
import com.goldang.goldangtime.entity.Pets;
import com.goldang.goldangtime.entity.Users;
import com.goldang.goldangtime.repository.PetRepository;
import com.goldang.goldangtime.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final UserRepository userRepository;

    // 반려동물 정보 저장
    @Transactional
    public Pets savePet(PetRequestDto petRequestDto) {
        // 사용자 조회
        Users user = userRepository.findById(petRequestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Pets 엔티티 생성
        Pets pet = Pets.builder()
                .user(user)
                .petName(petRequestDto.getPetName())
                .breed(petRequestDto.getBreed())
                .age(petRequestDto.getAge())
                .gender(petRequestDto.getGender())
                .residence(petRequestDto.getResidence())
                .feature(petRequestDto.getFeature())
                .petPhoto(petRequestDto.getPetPhoto())
                .build();

        return petRepository.save(pet);
    }


}

