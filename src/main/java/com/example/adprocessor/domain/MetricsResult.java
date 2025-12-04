package com.example.adprocessor.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MetricsResult {
    private Integer appId;
    private String countryCode;
    private long impressions;
    private long clicks;
    private double revenue;
}