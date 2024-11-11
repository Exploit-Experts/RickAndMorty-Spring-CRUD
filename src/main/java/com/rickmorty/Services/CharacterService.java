package com.rickmorty.Services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rickmorty.DTO.ApiResponseDto;
import com.rickmorty.DTO.CharacterDto;
import com.rickmorty.DTO.InfoDto;
import com.rickmorty.Utils.Config;
import lombok.extern.slf4j.Slf4j;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CharacterService {

    private static final String URL_API = "https://rickandmortyapi.com/api";

    public ApiResponseDto<CharacterDto> findAllCharacters(Integer page) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            String urlWithPage = URL_API + "/character" + (page != null ? "?page=" + page : "");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlWithPage))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper();

            ApiResponseDto<CharacterDto> apiResponseDto = objectMapper.readValue(response.body(),
                    new TypeReference<ApiResponseDto<CharacterDto>>() {
                    });
            return rewriteApiResponse(apiResponseDto);
        } catch (Exception e) {
            log.error("Erro ao buscar personagens: " + e.getMessage(), e);
        }
        return null;
    }

    public ResponseEntity<byte[]> findCharacterAvatar(String id) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL_API + "/character/" + id))
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
                    .uri(URI.create(URL_API + "/character/" + id))
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

    private ApiResponseDto<CharacterDto> rewriteApiResponse(ApiResponseDto<CharacterDto> apiResponseDto) {
        InfoDto updatedInfo = rewriteInfoDto(apiResponseDto.info());

        List<CharacterDto> updatedResults = new ArrayList<>();
        for (CharacterDto character : apiResponseDto.results()) {
            CharacterDto updatedCharacter = rewriteCharacterDto(character);
            updatedResults.add(updatedCharacter);
        }
        return new ApiResponseDto<>(updatedInfo, updatedResults);
    }

    private InfoDto rewriteInfoDto(InfoDto originalInfo) {
        String nextUrl = Optional.ofNullable(originalInfo.next())
                .map(next -> next.replace("https://rickandmortyapi.com/api/character",
                        Config.base_url + "/characters"))
                .orElse(null);
    
        String prevUrl = Optional.ofNullable(originalInfo.prev())
                .map(prev -> prev.replace("https://rickandmortyapi.com/api/character",
                        Config.base_url + "/characters"))
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
                character.image().replace("https://rickandmortyapi.com/api/character/",
                        Config.base_url + "/characters/"),
                character.episode().stream()
                        .map(episode -> episode.replace("https://rickandmortyapi.com/api/episode/",
                                Config.base_url + "/episodes/"))
                        .collect(Collectors.toList()));
    }

    private byte[] downloadImage(URL imageUrl) throws Exception {
        try (InputStream in = imageUrl.openStream()) {
            return in.readAllBytes();
        }
    }
}