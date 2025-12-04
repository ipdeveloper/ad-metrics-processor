package com.example.adprocessor.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MetricsResult {
    @JsonProperty("app_id")
    private Integer appId;
    @JsonProperty("country_code")
    private String countryCode;
    private long impressions;
    private long clicks;
    private double revenue;
}