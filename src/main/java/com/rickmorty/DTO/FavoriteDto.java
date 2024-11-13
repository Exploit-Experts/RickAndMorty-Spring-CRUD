package com.rickmorty.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rickmorty.enums.ItemType;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FavoriteDto(
        @JsonProperty("apiId") Long apiId,
        @JsonProperty("itemType") ItemType itemType,
        @JsonProperty("userId") Long userId
) {
}
