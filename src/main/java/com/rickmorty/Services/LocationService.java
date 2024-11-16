package com.rickmorty.Services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rickmorty.DTO.ApiResponseDto;
import com.rickmorty.DTO.InfoDto;
import com.rickmorty.DTO.LocationDto;
import com.rickmorty.Utils.Config;
import com.rickmorty.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final HttpClient client;

    @Autowired
    public LocationService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.client = HttpClient.newHttpClient();
    }

    public ApiResponseDto findAllLocations(Integer page) {
        if (page != null && page < 1) throw new InvalidParameterException("Page precisa ser um número positivo.");
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(config.getApiBaseUrl() + "/location/?page=" + page))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.body() == null || response.body().isEmpty() || response.statusCode() == 404) throw new PageNotFoundException();

            ApiResponseDto<LocationDto> apiResponseDto = objectMapper.readValue(response.body(),
                    new TypeReference<ApiResponseDto<LocationDto>>() {
                    });
            return rewriteApiResponse(apiResponseDto);
        }catch (PageNotFoundException e) {
            throw new PageNotFoundException();
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    public LocationDto getLocationById(Long id) {
        if (id == null || id < 1) throw new InvalidIdException();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(config.getApiBaseUrl() + "/location/" + id))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.body().isEmpty() || response.statusCode() == 404) throw new LocationNotFoundException("Localizações não encontradas");

            LocationDto location = objectMapper.readValue(response.body(), LocationDto.class);
            return rewriteLocationDto(location);
        } catch (LocationNotFoundException ex) {
            throw new LocationNotFoundException(ex.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private ApiResponseDto<LocationDto> rewriteApiResponse(ApiResponseDto<LocationDto> apiResponseDto) {
        InfoDto updatedInfo = rewriteInfoDto(apiResponseDto.info());

        List<LocationDto> updatedResults = new ArrayList<>();
        for (LocationDto location : apiResponseDto.results()) {
            LocationDto updatedLocation = rewriteLocationDto(location);
            updatedResults.add(updatedLocation);
        }
        return new ApiResponseDto<>(updatedInfo, updatedResults);
    }


    private InfoDto rewriteInfoDto(InfoDto originalInfo) {
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