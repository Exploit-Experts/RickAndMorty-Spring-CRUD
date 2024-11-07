package com.rickmorty.DTO;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rickmorty.Utils.Config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EpisodeDto(
        Integer id,

        String name,
        @JsonAlias("episode")
        String episodeCode,
        @JsonAlias("air_date")
        String releaseDate,

        List<String>characters ) {
        public EpisodeDto {
                List<String> modifiedCharacters = new ArrayList<>();

                characters.forEach(characterUrl ->
                        modifiedCharacters.add(characterUrl.
                                replace("https://rickandmortyapi.com/api/character/",
                                        Config.base_url + "/characters/"))
                );
                characters = modifiedCharacters;
        }
}
