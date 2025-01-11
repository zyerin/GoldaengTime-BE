package com.goldang.goldangtime.dto;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import lombok.Builder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoundPostDto {
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private String location;
    private String foundPhoto;
    private Integer scrap;
    private LocalDateTime createdAt;
}