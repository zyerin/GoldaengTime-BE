package com.goldang.goldangtime.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PetRequestDto {
    private String petName;
    private String breed;
    private Integer age;
    private String gender;
    private String residence;
    private String feature;
    private String petPhoto;

    private Long userId; // 사용자 ID
}

