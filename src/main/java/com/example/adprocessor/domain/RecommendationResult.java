package com.example.adprocessor.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RecommendationResult {
    @JsonProperty("app_id")
    private Integer appId;
    @JsonProperty("country_code")
    private String countryCode;
    @JsonProperty("recommended_advertiser_ids")
    private List<Integer> recommendedAdvertiserIds;
}