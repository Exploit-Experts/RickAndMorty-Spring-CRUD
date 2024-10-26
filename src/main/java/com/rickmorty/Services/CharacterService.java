package com.rickmorty.Services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rickmorty.DTO.ApiResponseDto;
import com.rickmorty.DTO.CharacterDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Slf4j
@Service
public class CharacterService {

    private static final String URL_API = "https://rickandmortyapi.com/api";

    public CharacterDto getCharacterById(String id) {
        log.info("Buscando personagem com o id [" + id + "]");
        CharacterDto characterDto = null;

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL_API + "/character/" + id))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper();
            characterDto = objectMapper.readValue(response.body(), CharacterDto.class);

        } catch (Exception e) {
            log.error("Erro: " + e.getMessage());
        }
        return characterDto;
    }

    public List<CharacterDto> findAllCharacter() {
        try{
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL_API + "/character/"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper();

            ApiResponseDto<CharacterDto> apiResponseDto = objectMapper.readValue(response.body(),
                    new TypeReference<ApiResponseDto<CharacterDto>>() {});
            return apiResponseDto.results();
        } catch (Exception e) {
            log.error("Um erro aconteceu"+e.getMessage());
        }
        return List.of();
    }
}