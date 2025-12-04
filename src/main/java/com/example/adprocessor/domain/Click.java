package com.example.adprocessor.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Click {
    @JsonProperty("impression_id")
    private String impressionId;
    private Double revenue;
}