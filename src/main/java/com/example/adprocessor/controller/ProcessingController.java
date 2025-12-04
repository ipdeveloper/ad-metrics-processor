package com.example.adprocessor.controller;

import com.example.adprocessor.domain.Click;
import com.example.adprocessor.domain.Impression;
import com.example.adprocessor.domain.MetricsResult;
import com.example.adprocessor.domain.RecommendationResult;
import com.example.adprocessor.service.MetricsService;
import com.example.adprocessor.service.ProcessService;
import com.example.adprocessor.service.RecommendationService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProcessingController {

    private final ObjectMapper objectMapper;
    private final ProcessService processService;


    @PostMapping(path = "/process", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> process(
            @RequestParam(value = "impressions", required = false) List<MultipartFile> impressions,
            @RequestParam(value = "clicks", required = false) List<MultipartFile> clicks) throws Exception {

        List<Impression> impressionList = new ArrayList<>();
        List<Click> clickList = new ArrayList<>();

        if (impressions != null) {
            for (MultipartFile f : impressions) {
                impressionList.addAll(objectMapper.readValue(f.getInputStream(), new TypeReference<List<Impression>>() {
                }));
            }
        }

        if (clicks != null) {
            for (MultipartFile f : clicks) {
                clickList.addAll(objectMapper.readValue(f.getInputStream(), new TypeReference<List<Click>>() {
                }));
            }
        }


        return ResponseEntity.ok().body(
                processService.process(impressionList, clickList)
        );
    }
}