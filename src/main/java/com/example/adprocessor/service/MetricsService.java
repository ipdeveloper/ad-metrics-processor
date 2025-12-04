package com.example.adprocessor.service;

import com.example.adprocessor.domain.Click;
import com.example.adprocessor.domain.Impression;
import com.example.adprocessor.domain.MetricsResult;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MetricsService {

    public List<MetricsResult> calculate(List<Impression> impressions, List<Click> clicks) {
        // Map impression id -> Impression

        Map<String, Impression> impById = impressions.stream().collect(Collectors.toMap(Impression::getId, i -> i, (a,b)->a));

        // impressions grouped by (app,country)
        Map<List<Object>, Long> impressionCounts = impressions.stream()
                .collect(Collectors.groupingBy(i -> Arrays.asList(i.getAppId(), i.getCountryCode()), Collectors.counting()));

        // clicks grouped by (app,country)
        Map<List<Object>, Long> clickCounts = clicks.stream()
                .map(c -> impById.get(c.getImpressionId()))
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(i -> Arrays.asList(i.getAppId(), i.getCountryCode()), Collectors.counting()));

        // revenue grouped by (app,country)
        Map<List<Object>, Double> revenueSums = clicks.stream()
                .map(c -> Map.entry(impById.get(c.getImpressionId()), c.getRevenue()))
                .filter(e -> e.getKey() != null && e.getValue() != null)
                .collect(Collectors.groupingBy(e -> Arrays.asList(e.getKey().getAppId(), e.getKey().getCountryCode()),
                        Collectors.summingDouble(Map.Entry::getValue)));

        // gather all keys
        Set<List<Object>> keys = new HashSet<>();
        keys.addAll(impressionCounts.keySet());
        keys.addAll(clickCounts.keySet());
        keys.addAll(revenueSums.keySet());

        List<MetricsResult> results = new ArrayList<>();
        for (List<Object> k : keys) {
            Integer appId = (Integer) k.get(0);
            String country = (String) k.get(1);
            long imps = impressionCounts.getOrDefault(k, 0L);
            long cls = clickCounts.getOrDefault(k, 0L);
            double rev = revenueSums.getOrDefault(k, 0.0);
            results.add(new MetricsResult(appId, country, imps, cls, rev));
        }

        // optional: sort by app then country
        results.sort(Comparator.comparing(MetricsResult::getAppId).thenComparing(MetricsResult::getCountryCode));
        return results;
    }
}