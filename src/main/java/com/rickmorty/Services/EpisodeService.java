package com.rickmorty.Services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rickmorty.DTO.ApiResponseDto;
import com.rickmorty.DTO.EpisodeDto;
import com.rickmorty.DTO.InfoDto;
import com.rickmorty.Utils.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EpisodeService {

    @Autowired
    Config config;

    public ApiResponseDto<EpisodeDto> findAllEpisodes(Integer page, String name, String episode, String sort) {
        try {
            StringBuilder urlBuilder = new StringBuilder(config.getApiBaseUrl() + "/episode?");
            if (page != null) urlBuilder.append("page=").append(page).append("&");
            if (name != null) urlBuilder.append("name=").append(page).append("&");
            if (episode != null) urlBuilder.append("episode=").append(page).append("&");

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlBuilder.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper();

            ApiResponseDto<EpisodeDto> apiResponseDto = objectMapper.readValue(response.body(),
                    new TypeReference<ApiResponseDto<EpisodeDto>>() {
                    });
            return rewriteApiResponse(apiResponseDto, sort);
        } catch (Exception e) {
            log.error("Erro ao buscar episódios: " + e.getMessage(), e);
        }
        return null;
    }

    public EpisodeDto findEpisodeById(String id) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(config.getApiBaseUrl() + "/episode/" + id))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper();

            EpisodeDto episode = objectMapper.readValue(response.body(), EpisodeDto.class);
            return rewriteEpisodeDto(episode);
        } catch (Exception e) {
            log.error("Erro ao buscar episódio por ID: " + e.getMessage(), e);
        }
        return null;
    }

    private ApiResponseDto<EpisodeDto> rewriteApiResponse(ApiResponseDto<EpisodeDto> apiResponseDto, String sort) {
        InfoDto updatedInfo = rewriteInfoDto(apiResponseDto.info());

        List<EpisodeDto> updatedResults = apiResponseDto.results().stream()
                .map(this::rewriteEpisodeDto)
                .sorted((e1, e2) -> compareEpisodes(e1, e2, sort)) // Ordena com base no sort, se necessário
                .collect(Collectors.toList());

        return new ApiResponseDto<>(updatedInfo, updatedResults);
    }

    private int compareEpisodes(EpisodeDto e1, EpisodeDto e2, String sort) {
        if (sort == null || sort.isEmpty()) {
            return 0;
        }

        switch (sort.toLowerCase()) {
            case "name":
                return e1.name().compareToIgnoreCase(e2.name());
            case "name_desc":
                return e2.name().compareToIgnoreCase(e1.name());
            case "air_date":
                return e1.releaseDate().compareTo(e2.releaseDate());
            case "air_date_desc":
                return e2.releaseDate().compareTo(e1.releaseDate());
            case "episode_code":
                return e1.episodeCode().compareTo(e2.episodeCode());
            case "episode_code_desc":
                return e2.episodeCode().compareTo(e1.episodeCode());
            default:
                return 0;
        }
    }

    private InfoDto rewriteInfoDto(InfoDto originalInfo) {
        String nextUrl = Optional.ofNullable(originalInfo.next())
                .map(next -> next.replace(config.getApiBaseUrl()+ "/episode",
                        config.getLocalBaseUrl() + "/episodes"))
                .orElse(null);
    
        String prevUrl = Optional.ofNullable(originalInfo.prev())
                .map(prev -> prev.replace(config.getApiBaseUrl() + "/episode",
                        config.getLocalBaseUrl() + "/episodes"))
                .orElse(null);
    
        return new InfoDto(
                originalInfo.count(),
                originalInfo.pages(),
                nextUrl,
                prevUrl);
    }

    private EpisodeDto rewriteEpisodeDto(EpisodeDto episode) {
        return new EpisodeDto(
                episode.id(),
                episode.name(),
                episode.episodeCode(),
                episode.releaseDate(),
                episode.characters().stream()
                        .map(character -> character.replace(config.getApiBaseUrl() + "/character/",
                                config.getLocalBaseUrl() + "/characters/"))
                        .collect(Collectors.toList()));
    }
}