package com.goldang.goldangtime.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class MatchingDto {
    private Long lostPostId;
    private Long foundPostId;
    private float similarity;
}
