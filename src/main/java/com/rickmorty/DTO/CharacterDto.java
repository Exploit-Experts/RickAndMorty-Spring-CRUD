package com.rickmorty.DTO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rickmorty.Utils.Config;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public record CharacterDto(
        Integer id,
        @JsonProperty("name") String name,
        @JsonProperty("status") String status,
        @JsonProperty("species") String species,
        @JsonProperty("type") String type,
        @JsonProperty("gender") String gender,
        @JsonProperty("image") String image,
        @JsonProperty("episode") List<String> episode
) {
        public CharacterDto {
                List<String> modifiedEpisodes = episode;
                modifiedEpisodes.replaceAll(episodeUrl -> episodeUrl.replace("https://rickandmortyapi.com/api/episode/",Config.base_url+"/episodes/"));
        }
}