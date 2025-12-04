package com.example.adprocessor.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RecommendationResult {
    private Integer appId;
    private String countryCode;
    private List<Integer> recommendedAdvertiserIds;
}