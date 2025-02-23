package com.rickmorty.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rickmorty.Utils.Config;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LocationDto(
        @JsonProperty("id") Integer id,
        @JsonProperty("name") String name,
        @JsonProperty("type") String type,
        @JsonProperty("dimension") String dimension,
        @JsonProperty("residents") List<String> residents,
        @JsonProperty("url") String url
) {
        public LocationDto {
                id = (id == null) ? 0 : id;
                name = (name == null) ? "Unknown" : name;
                type = (type == null) ? "Unknown" : type;
                dimension = (dimension == null) ? "Unknown" : dimension;
                url = (url == null) ? "" : url;

                if (residents == null) {
                        residents = List.of();
                } else {
                        List<String> modifiedResidents = new ArrayList<>();
                        residents.forEach(residentUrl ->
                                modifiedResidents.add(residentUrl.replace(
                                        "https://rickandmortyapi.com/api/character/",
                                        Config.base_url + "/characteres/"))
                        );
                        residents = modifiedResidents;
                }
        }
}