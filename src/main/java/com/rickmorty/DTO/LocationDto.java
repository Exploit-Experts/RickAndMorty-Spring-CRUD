package com.rickmorty.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rickmorty.Utils.Config;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LocationDto(
        Integer id,
        @JsonProperty("name")
        String name,
        @JsonProperty("type")
        String type,
        @JsonProperty("dimension")
        String dimension,
        @JsonProperty("residents")
        List<String> residents,
        @JsonProperty("url")
        String url
) {

}
