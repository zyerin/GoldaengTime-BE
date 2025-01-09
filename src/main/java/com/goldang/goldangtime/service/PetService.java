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
    public Pets savePet(PetRequestDto petRequestDto, String email) {
        // 사용자 인증
        Users user = userRepository.findByEmail(email)
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

    // 반려동물 정보 수정
    @Transactional
    public Pets updatePet(Long petId, PetRequestDto petRequestDto, String email) {
        // 반려동물 조회
        Pets pet = petRepository.findById(petId)
                .orElseThrow(() -> new IllegalArgumentException("Pet not found"));

        // 요청 사용자와 반려동물 소유자가 동일한지 확인
        if (!pet.getUser().getEmail().equals(email)) {
            throw new SecurityException("You do not have permission to modify this pet.");
        }

        // 반려동물 정보 수정
        pet.setPetName(petRequestDto.getPetName());
        pet.setBreed(petRequestDto.getBreed());
        pet.setAge(petRequestDto.getAge());
        pet.setGender(petRequestDto.getGender());
        pet.setResidence(petRequestDto.getResidence());
        pet.setFeature(petRequestDto.getFeature());
        pet.setPetPhoto(petRequestDto.getPetPhoto());

        return petRepository.save(pet);
    }

    // 반려동물 정보 삭제
    @Transactional
    public void deletePet(Long petId, String email) {
        // 반려동물 조회
        Pets pet = petRepository.findById(petId)
                .orElseThrow(() -> new IllegalArgumentException("Pet not found"));

        // 요청 사용자와 반려동물 소유자가 동일한지 확인
        if (!pet.getUser().getEmail().equals(email)) {
            throw new SecurityException("You do not have permission to delete this pet.");
        }

        // 삭제
        petRepository.delete(pet);
    }
}

