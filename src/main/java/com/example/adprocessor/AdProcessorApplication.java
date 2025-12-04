package com.example.adprocessor;

import com.example.adprocessor.domain.Click;
import com.example.adprocessor.domain.Impression;
import com.example.adprocessor.domain.MetricsResult;
import com.example.adprocessor.domain.RecommendationResult;
import com.example.adprocessor.service.FileReaderService;
import com.example.adprocessor.service.MetricsService;
import com.example.adprocessor.service.ProcessService;
import com.example.adprocessor.service.RecommendationService;
import com.example.adprocessor.util.JsonWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class AdProcessorApplication implements CommandLineRunner {

    private final FileReaderService fileReaderService;
    private final ProcessService processService;

    public static void main(String[] args) {
        SpringApplication.run(AdProcessorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // If no args: just start as REST service.
        if (args == null || args.length == 0) {
            System.out.println("No CLI args provided — running in REST mode. Use -Dspring-boot.run.arguments=<...> to pass file lists.");
            return;
        }

        // CLI usage: two groups separated by "--": impressions-files -- clicks-files
        // Example: java -jar app.jar impressions1.json impressions2.json -- clicks1.json clicks2.json

        List<String> impressionFiles = List.of();
        List<String> clickFiles = List.of();

        boolean seenSep = false;
        for (String a : args) {
            if ("--".equals(a)) { seenSep = true; continue; }
            if (!seenSep) {
                impressionFiles = new java.util.ArrayList<>(impressionFiles);
                ( impressionFiles).add(a);
            } else {
                clickFiles = new java.util.ArrayList<>(clickFiles);
                ( clickFiles).add(a);
            }
        }

        List<Impression> impressionList = fileReaderService.readImpressions(impressionFiles);
        List<Click> clickList = fileReaderService.readClicks(clickFiles);

        HashMap<String, Object> process = processService.process(impressionList, clickList);
        List<MetricsResult> metricsResults = (List<MetricsResult>) process.get("metrics");
        List<RecommendationResult> recommendationResults=(List<RecommendationResult>) process.get("recommendations");
        JsonWriter.write(metricsResults, "metrics.json");

        JsonWriter.write(recommendationResults, "recommendations.json");

        System.out.println("Processing done. Outputs: metrics.json, recommendations.json");
    }
}