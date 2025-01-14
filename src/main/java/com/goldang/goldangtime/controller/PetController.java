package com.goldang.goldangtime.controller;

import com.goldang.goldangtime.dto.PetRequestDto;
import com.goldang.goldangtime.entity.CustomUserDetails;
import com.goldang.goldangtime.entity.Pets;
import com.goldang.goldangtime.service.PetService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pets")
@RequiredArgsConstructor
@Slf4j
public class PetController {

    private final PetService petService;

    // 반려동물 정보 저장
    @PostMapping("/save")
    @Operation(summary = "반려동물 정보 저장")
    public ResponseEntity<Pets> savePet(@RequestBody PetRequestDto petRequestDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        log.info("Received PetRequestDto: {}", petRequestDto);
        String email = userDetails.getUsername();
        Pets pet = petService.savePet(petRequestDto, email);
        return ResponseEntity.ok(pet);
    }

    // 반려동물 정보 수정
    @PutMapping("/{petId}")
    @Operation(summary = "반려동물 정보 수정")
    public ResponseEntity<Pets> updatePet(@PathVariable Long petId, @RequestBody PetRequestDto petRequestDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        String email = userDetails.getUsername();
        Pets updatedPet = petService.updatePet(petId, petRequestDto, email);
        return ResponseEntity.ok(updatedPet);
    }

    // 반려동물 정보 삭제
    @DeleteMapping("/{petId}")
    @Operation(summary = "반려동물 정보 삭제")
    public ResponseEntity<Void> deletePet(@PathVariable Long petId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        String email = userDetails.getUsername();
        petService.deletePet(petId, email);
        return ResponseEntity.noContent().build();
    }

}

