package com.rickmorty.Services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rickmorty.DTO.ApiResponseDto;
import com.rickmorty.DTO.CharacterDto;
import com.rickmorty.DTO.InfoDto;
import com.rickmorty.Utils.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CharacterService {

    @Autowired
    Config config;

    public ApiResponseDto<CharacterDto> findAllCharacters(Integer page, String name, String status, String species, String type, String gender, String sort) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            StringBuilder urlBuilder = new StringBuilder(config.getApiBaseUrl() + "/character?");
            if (page != null) urlBuilder.append("page=").append(page).append("&");
            if (name != null) urlBuilder.append("name=").append(name).append("&");
            if (status != null) urlBuilder.append("status=").append(status).append("&");
            if (species != null) urlBuilder.append("species=").append(species).append("&");
            if (type != null) urlBuilder.append("type=").append(type).append("&");
            if (gender != null) urlBuilder.append("gender=").append(gender).append("&");

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlBuilder.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper();

            ApiResponseDto<CharacterDto> apiResponseDto = objectMapper.readValue(response.body(), new TypeReference<ApiResponseDto<CharacterDto>>() {});
            return rewriteApiResponse(apiResponseDto, sort);
        } catch (Exception e) {
            log.error("Erro ao buscar personagens: " + e.getMessage(), e);
        }
        return null;
    }

    private ApiResponseDto<CharacterDto> rewriteApiResponse(ApiResponseDto<CharacterDto> apiResponseDto, String sort) {
        InfoDto updatedInfo = rewriteInfoDto(apiResponseDto.info());

        List<CharacterDto> updatedResults = apiResponseDto.results().stream()
                .map(this::rewriteCharacterDto)
                .sorted((c1, c2) -> compareCharacters(c1, c2, sort))
                .collect(Collectors.toList());

        return new ApiResponseDto<>(updatedInfo, updatedResults);
    }

    private int compareCharacters(CharacterDto c1, CharacterDto c2, String sort) {
        if (sort == null || sort.isEmpty()) {
            return 0;
        }
        switch (sort.toLowerCase()) {
            case "name_asc":
                return c1.name().compareToIgnoreCase(c2.name());
            case "name_desc":
                return c2.name().compareToIgnoreCase(c1.name());
            case "status_asc":
                return c1.status().compareToIgnoreCase(c2.status());
            case "status_desc":
                return c2.status().compareToIgnoreCase(c1.status());
            default:
                return 0;
        }
    }

    public ResponseEntity<byte[]> findCharacterAvatar(String id) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(config.getApiBaseUrl() + "/character/" + id))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper();
            String imageUrl = objectMapper.readTree(response.body()).get("image").asText();
            byte[] imageBytes = downloadImage(URI.create(imageUrl).toURL());

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "image/jpeg");

            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Erro ao buscar avatar do personagem: " + e.getMessage(), e);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public CharacterDto findACharacterById(String id) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(config.getApiBaseUrl() + "/character/" + id))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper();

            CharacterDto character = objectMapper.readValue(response.body(), CharacterDto.class);
            return rewriteCharacterDto(character);
        } catch (Exception e) {
            log.error("Erro ao buscar personagem por ID: " + e.getMessage(), e);
        }
        return null;
    }

    private InfoDto rewriteInfoDto(InfoDto originalInfo) {
        String nextUrl = Optional.ofNullable(originalInfo.next())
                .map(next -> next.replace(config.getApiBaseUrl() + "/character",
                        config.getLocalBaseUrl() + "/characters"))
                .orElse(null);
    
        String prevUrl = Optional.ofNullable(originalInfo.prev())
                .map(prev -> prev.replace(config.getApiBaseUrl() + "/character",
                        config.getLocalBaseUrl() + "/characters"))
                .orElse(null);
    
        return new InfoDto(
                originalInfo.count(),
                originalInfo.pages(),
                nextUrl,
                prevUrl);
    }

    private CharacterDto rewriteCharacterDto(CharacterDto character) {
        return new CharacterDto(
                character.id(),
                character.name(),
                character.status(),
                character.species(),
                character.type(),
                character.gender(),
                character.image().replace(config.getApiBaseUrl() +"/character/",
                        config.getLocalBaseUrl() + "/characters/"),
                character.episode().stream()
                        .map(episode -> episode.replace(config.getApiBaseUrl() + "/episode/",
                                config.getLocalBaseUrl() + "/episodes/"))
                        .collect(Collectors.toList()));
    }

    private byte[] downloadImage(URL imageUrl) throws Exception {
        try (InputStream in = imageUrl.openStream()) {
            return in.readAllBytes();
        }
    }
}
