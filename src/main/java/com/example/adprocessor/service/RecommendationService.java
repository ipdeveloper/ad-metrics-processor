package com.example.adprocessor.service;

import com.example.adprocessor.domain.Click;
import com.example.adprocessor.domain.Impression;
import com.example.adprocessor.domain.RecommendationResult;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    public List<RecommendationResult> calculate(List<Impression> impressions, List<Click> clicks) {
        // Map impression id -> Impression
        Map<String, Impression> impById = impressions.stream().collect(Collectors.toMap(Impression::getId, i -> i, (a,b)->a));

        // Group impressions by (app,country,advertiser) -> impression count
        Map<List<Object>, Long> impCounts = impressions.stream()
                .collect(Collectors.groupingBy(i -> Arrays.asList(i.getAppId(), i.getCountryCode(), i.getAdvertiserId()), Collectors.counting()));

        // Group revenue by (app,country,advertiser)
        Map<List<Object>, Double> revenueBy = new HashMap<>();
        for (Click c : clicks) {
            Impression i = impById.get(c.getImpressionId());
            if (i == null) continue; // orphan click
            List<Object> key = Arrays.asList(i.getAppId(), i.getCountryCode(), i.getAdvertiserId());
            revenueBy.merge(key, c.getRevenue() == null ? 0.0 : c.getRevenue(), Double::sum);
        }

        // For each (app,country) compute top 5 advertisers by revenue_per_impression
        Map<List<Object>, List<Integer>> resultMap = new HashMap<>();

        // Collect all (app,country) pairs
        Set<List<Object>> appCountryPairs = impCounts.keySet().stream()
                .map(k -> Arrays.asList(k.get(0), k.get(1)))
                .collect(Collectors.toSet());

        for (List<Object> pair : appCountryPairs) {
            Integer appId = (Integer) pair.get(0);
            String country = (String) pair.get(1);

            // candidates: all advertisers for this pair
            List<Integer> advertisers = impCounts.keySet().stream()
                    .filter(k -> Objects.equals(k.get(0), appId) && Objects.equals(k.get(1), country))
                    .map(k -> (Integer) k.get(2))
                    .distinct()
                    .collect(Collectors.toList());

            // compute revenue_per_impression for each advertiser
            List<Map.Entry<Integer, Double>> scores = new ArrayList<>();
            for (Integer adv : advertisers) {
                List<Object> key = Arrays.asList(appId, country, adv);
                double revenue = revenueBy.getOrDefault(key, 0.0);
                long imps = impCounts.getOrDefault(key, 0L);
                double score = imps == 0 ? 0.0 : revenue / imps;
                scores.add(Map.entry(adv, score));
            }

            // top 5 by score desc
            List<Integer> top5 = scores.stream()
                    .sorted((a,b) -> Double.compare(b.getValue(), a.getValue()))
                    .limit(5)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            resultMap.put(pair, top5);
        }

        // convert to RecommendationResult list
        List<RecommendationResult> out = new ArrayList<>();
        for (var e : resultMap.entrySet()) {
            Integer appId = (Integer) e.getKey().get(0);
            String country = (String) e.getKey().get(1);
            out.add(new RecommendationResult(appId, country, e.getValue()));
        }

        out.sort(Comparator.comparing(RecommendationResult::getAppId).thenComparing(RecommendationResult::getCountryCode));
        return out;
    }
}