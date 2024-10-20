package com.rickmorty.DTO;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EpisodeDto(
        Integer id,
        @JsonAlias("name")
        String nome,
        @JsonAlias("episode")
        String codEpisode,
        @JsonAlias("air_date")
        String lancamento,
        @JsonAlias("characters")
        List<String>characters ) {
}
