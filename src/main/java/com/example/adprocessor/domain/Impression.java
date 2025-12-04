package com.example.adprocessor.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Impression {

    private String id;
    @JsonProperty("app_id")
    private Integer appId;
    @JsonProperty("country_code")
    private String countryCode;
    @JsonProperty("advertiser_id")
    private Integer advertiserId;
}