package com.example.adprocessor.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.List;

public class JsonWriter {

    private static final ObjectMapper om = new ObjectMapper();

    public static <T> void write(List<T> data, String filename) {
        try {
            om.writerWithDefaultPrettyPrinter().writeValue(new File(filename), data);
        } catch (Exception e) {
            throw new RuntimeException("Failed to write JSON file: " + filename, e);
        }
    }
}