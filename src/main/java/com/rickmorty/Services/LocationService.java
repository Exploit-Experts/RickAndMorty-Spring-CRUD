package com.rickmorty.Services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rickmorty.DTO.ApiResponseDto;
import com.rickmorty.DTO.LocationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.logging.ErrorManager;


@Slf4j
@Service
public class LocationService {

    private static final String URL_API = "https://rickandmortyapi.com/api";

    private final ObjectMapper objectMapper;

    @Autowired
    public LocationService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<LocationDto> findAllLocations() {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL_API + "/location/"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ApiResponseDto<LocationDto> apiResponseDto = objectMapper.readValue(response.body(),
                    new TypeReference<ApiResponseDto<LocationDto>>() {});
            return apiResponseDto.results();
        } catch (Exception e) {
            ErrorManager log = null;
            return List.of();
        }
    }

    public LocationDto getLocationById(String id) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL_API + "/location/" + id))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return objectMapper.readValue(response.body(), LocationDto.class);
        } catch (Exception e) {
             System.out.println("Um erro ocorreu ao buscar a localização com ID " + id + ": " + e.getMessage());

        }
        return null;
    }
}