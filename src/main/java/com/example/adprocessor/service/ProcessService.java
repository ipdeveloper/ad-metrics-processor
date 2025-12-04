package com.example.adprocessor.service;

import com.example.adprocessor.domain.Click;
import com.example.adprocessor.domain.Impression;
import com.example.adprocessor.domain.MetricsResult;
import com.example.adprocessor.domain.RecommendationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProcessService {
    private final MetricsService metricsService;
    private final RecommendationService recommendationService;

    public java.util.HashMap<String, Object> process(List<Impression> impressionList, List<Click> clickList) {
        List<MetricsResult> metricsResults = metricsService.calculate(impressionList, clickList);
        List<RecommendationResult> recommendationResults = recommendationService.calculate(impressionList, clickList);
        return new java.util.HashMap<String, Object>() {{
            put("metrics", metricsResults);
            put("recommendations", recommendationResults);
        }};

    }
}
