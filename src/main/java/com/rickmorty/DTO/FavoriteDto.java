package com.rickmorty.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rickmorty.enums.ItemType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FavoriteDto(
        @Min(value = 1, message = "{favorite.ApiIdHigherThanZero}") @JsonProperty("apiId") Long apiId,
        @JsonProperty("itemType") ItemType itemType,
        @Min(value = 1, message = "{favorite.UserIdHigherThanZero}") @JsonProperty("userId") Long userId
) {
}
