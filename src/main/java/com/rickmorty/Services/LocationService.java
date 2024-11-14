package com.rickmorty.Services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rickmorty.DTO.ApiResponseDto;
import com.rickmorty.DTO.InfoDto;
import com.rickmorty.DTO.LocationDto;
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
import java.util.stream.Collectors;


@Slf4j
@Service
public class LocationService {

    @Autowired
    Config config;

    private final ObjectMapper objectMapper;

    @Autowired
    public LocationService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ApiResponseDto findAllLocations(Integer page) {
        try{
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(config.getApiBaseUrl() + "/location/" + (page != null ? "?page=" +page : "")))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ApiResponseDto<LocationDto> apiResponseDto = objectMapper.readValue(response.body(),
                    new TypeReference<ApiResponseDto<LocationDto>>() {});
            return RewriteApiResponse(apiResponseDto);
        } catch (Exception e) {
            log.error("Erro ao buscar localizações: " + e.getMessage(), e);
        }
        return null;
    }

    public LocationDto getLocationById(String id) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(config.getApiBaseUrl() + "/location/" + id))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return objectMapper.readValue(response.body(), LocationDto.class);
        } catch (Exception e) {
            log.error("Erro ao buscar localização por id: " + e.getMessage(), e);
        }
        return null;
    }

    private ApiResponseDto<LocationDto> RewriteApiResponse(ApiResponseDto<LocationDto> apiResponseDto) {
        InfoDto updatedInfo = RewriteInfoDto(apiResponseDto.info());

        List<LocationDto> updatedResults = new ArrayList<>();
        for (LocationDto location : apiResponseDto.results()) {
            LocationDto updatedLocation = rewriteLocationDto(location);
            updatedResults.add(updatedLocation);
        }
        return new ApiResponseDto<>(updatedInfo, updatedResults);
    }


    private InfoDto RewriteInfoDto(InfoDto originalInfo) {
        return new InfoDto(
                originalInfo.count(),
                originalInfo.pages(),
                originalInfo.next() != null ? originalInfo.next().replace(config.getApiBaseUrl() + "/location/", config.getLocalBaseUrl() + "/locations") : null,
                originalInfo.prev() != null ? originalInfo.prev().replace(config.getApiBaseUrl() + "/location/", config.getLocalBaseUrl() + "/locations") : null
        );
    }

    private LocationDto rewriteLocationDto(LocationDto location) {
        return new LocationDto(
                location.id(),
                location.name(),
                location.type(),
                location.dimension(),
                location.residents().stream()
                        .map(resident -> resident.replace(config.getApiBaseUrl() + "/character/",
                                config.getLocalBaseUrl() + "/characters/"))
                        .collect(Collectors.toList()),
                location.url().replace(config.getApiBaseUrl()+"/location/", config.getLocalBaseUrl() + "/locations/")
        );
    }


}