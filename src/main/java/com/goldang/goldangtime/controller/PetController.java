package com.goldang.goldangtime.controller;

import com.goldang.goldangtime.dto.PetRequestDto;
import com.goldang.goldangtime.entity.Pets;
import com.goldang.goldangtime.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @PostMapping("/save")
    public ResponseEntity<Pets> savePet(@RequestBody PetRequestDto petRequestDto) {
        Pets pet = petService.savePet(petRequestDto);
        return ResponseEntity.ok(pet);
    }
}

