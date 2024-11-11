package com.rickmorty.Services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rickmorty.DTO.ApiResponseDto;
import com.rickmorty.DTO.EpisodeDto;
import com.rickmorty.DTO.InfoDto;
import com.rickmorty.Utils.Config;
import lombok.extern.slf4j.Slf4j;
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

    private static final String URL_API = "https://rickandmortyapi.com/api";

    public ApiResponseDto<EpisodeDto> findAllEpisodes(Integer page) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            String urlWithPage = URL_API + "/episode" + (page != null ? "?page=" + page : "");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlWithPage))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper();

            ApiResponseDto<EpisodeDto> apiResponseDto = objectMapper.readValue(response.body(),
                    new TypeReference<ApiResponseDto<EpisodeDto>>() {
                    });
            return rewriteApiResponse(apiResponseDto);
        } catch (Exception e) {
            log.error("Erro ao buscar episódios: " + e.getMessage(), e);
        }
        return null;
    }

    public EpisodeDto findEpisodeById(String id) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL_API + "/episode/" + id))
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

    private ApiResponseDto<EpisodeDto> rewriteApiResponse(ApiResponseDto<EpisodeDto> apiResponseDto) {
        InfoDto updatedInfo = rewriteInfoDto(apiResponseDto.info());

        List<EpisodeDto> updatedResults = new ArrayList<>();
        for (EpisodeDto episode : apiResponseDto.results()) {
            EpisodeDto updatedEpisode = rewriteEpisodeDto(episode);
            updatedResults.add(updatedEpisode);
        }
        return new ApiResponseDto<>(updatedInfo, updatedResults);
    }

    private InfoDto rewriteInfoDto(InfoDto originalInfo) {
        String nextUrl = Optional.ofNullable(originalInfo.next())
                .map(next -> next.replace("https://rickandmortyapi.com/api/episode",
                        Config.base_url + "/episodes"))
                .orElse(null);
    
        String prevUrl = Optional.ofNullable(originalInfo.prev())
                .map(prev -> prev.replace("https://rickandmortyapi.com/api/episode",
                        Config.base_url + "/episodes"))
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
                        .map(character -> character.replace("https://rickandmortyapi.com/api/character/",
                                Config.base_url + "/characters/"))
                        .collect(Collectors.toList()));
    }
}