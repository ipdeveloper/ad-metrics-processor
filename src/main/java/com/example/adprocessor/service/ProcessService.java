package com.example.adprocessor.service;

import com.example.adprocessor.domain.Click;
import com.example.adprocessor.domain.Impression;
import com.example.adprocessor.domain.MetricsResult;
import com.example.adprocessor.domain.RecommendationResult;
import com.example.adprocessor.util.JsonValidatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProcessService {
    private final MetricsService metricsService;
    private final RecommendationService recommendationService;
    private final JsonValidatorService validatorService;

    public java.util.HashMap<String, Object> process(List<Impression> impressionList, List<Click> clickList) {
        List<Impression> validImpressions = validatorService.validateImpressions(impressionList);
        List<Click> validClicks = validatorService.validateClicks(clickList, validImpressions);
        List<MetricsResult> metricsResults = metricsService.calculate(validImpressions, validClicks);
        List<RecommendationResult> recommendationResults = recommendationService.calculate(validImpressions, validClicks);
        return new java.util.HashMap<String, Object>() {{
            put("metrics", metricsResults);
            put("recommendations", recommendationResults);
        }};

    }
}
