package com.example.adprocessor.util;

import com.example.adprocessor.domain.Click;
import com.example.adprocessor.domain.Impression;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JsonValidatorService {

    /**
     * Validate impressions: remove null/empty mandatory fields, log issues
     */
    public List<Impression> validateImpressions(List<Impression> impressions) {
        return impressions.stream()
                .filter(i -> {
                    boolean valid = true;
                    if (i.getAppId() == null) {
                        log.warn("Impression skipped: appId is null, id={}", i.getId());
                        valid = false;
                    }
                    if (i.getAdvertiserId() == null) {
                        log.warn("Impression skipped: advertiserId is null, id={}", i.getId());
                        valid = false;
                    }
                    if (i.getCountryCode() == null || i.getCountryCode().isBlank()) {
                        log.warn("Impression skipped: countryCode is null/blank, id={}", i.getId());
                        valid = false;
                    }
                    if (i.getId() == null || i.getId().isBlank()) {
                        log.warn("Impression skipped: id is null/blank");
                        valid = false;
                    }
                    return valid;
                })
                .collect(Collectors.toList());
    }

    /**
     * Validate clicks: remove null/empty mandatory fields, orphan clicks, negative revenue
     */
    public List<Click> validateClicks(List<Click> clicks, List<Impression> validImpressions) {
        var impressionIds = validImpressions.stream().map(Impression::getId).collect(Collectors.toSet());

        return clicks.stream()
                .filter(c -> {
                    boolean valid = true;
                    if (c.getImpressionId() == null || c.getImpressionId().isBlank()) {
                        log.warn("Click skipped: impressionId is null/blank");
                        valid = false;
                    } else if (!impressionIds.contains(c.getImpressionId())) {
                        log.warn("Click skipped: orphan click, impressionId={} not found", c.getImpressionId());
                        valid = false;
                    }
                    if (c.getRevenue() == null || c.getRevenue() < 0) {
                        log.warn("Click skipped: revenue null/negative, impressionId={}, revenue={}"
                                , c.getImpressionId(), c.getRevenue());
                        valid = false;
                    }
                    return valid;
                })
                .collect(Collectors.toList());
    }
}