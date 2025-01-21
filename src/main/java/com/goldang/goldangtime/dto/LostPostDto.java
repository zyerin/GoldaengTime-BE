package com.goldang.goldangtime.dto;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import lombok.Builder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LostPostDto{
    private Long id;
    private Long userId;
    private Long petId;
    private String title;
    private String description;
    private String location;
    private String lostPhoto;
    private String status;
    private Integer scrap;
    private LocalDateTime createdAt;
}
