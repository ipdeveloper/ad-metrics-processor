package com.example.adprocessor.service;

import com.example.adprocessor.domain.Click;
import com.example.adprocessor.domain.Impression;
import com.example.adprocessor.exception.InvalidEventException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileReaderService {

    private final ObjectMapper objectMapper;

    public List<Impression> readImpressions(List<String> paths) {
        if (paths == null || paths.isEmpty()) return Collections.emptyList();
        List<Impression> out = new ArrayList<>();
        for (String p : paths) {
            try {
                String content  = Files.readString(new File(p).toPath());
                var list = objectMapper.readValue(content, new TypeReference<List<Impression>>(){});
                out.addAll(list);
            } catch (Exception e) {
                throw new InvalidEventException("Failed to read impressions file: " + p, e);
            }
        }
        return out;
    }

    public List<Click> readClicks(List<String> paths) {
        if (paths == null || paths.isEmpty()) return Collections.emptyList();
        List<Click> out = new ArrayList<>();
        for (String p : paths) {
            try {
                var content = Files.readString(new File(p).toPath());
                var list = objectMapper.readValue(content, new TypeReference<List<Click>>(){});
                out.addAll(list);
            } catch (Exception e) {
                throw new InvalidEventException("Failed to read clicks file: " + p, e);
            }
        }
        return out;
    }
}