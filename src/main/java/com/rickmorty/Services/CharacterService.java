package com.rickmorty.Services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rickmorty.DTO.ApiResponseDto;
import com.rickmorty.DTO.CharacterDto;
import com.rickmorty.DTO.InfoDto;
import com.rickmorty.Utils.Config;
import com.rickmorty.exceptions.CharacterNotFoundException;
import com.rickmorty.exceptions.InvalidIdException;
import com.rickmorty.exceptions.InvalidPageNumberException;
import com.rickmorty.exceptions.PageNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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

    @Autowired
    Config config;

    public ApiResponseDto<CharacterDto> findAllCharacters(Integer page) {
        try {
            Integer validacao = page;
            HttpClient client = HttpClient.newHttpClient();
            String urlWithPage = config.getApiBaseUrl() + "/character" + (page != null ? "?page=" + page : "");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlWithPage))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper();

            ApiResponseDto<CharacterDto> apiResponseDto = objectMapper.readValue(response.body(),
                    new TypeReference<ApiResponseDto<CharacterDto>>() {
                    });

            if (response.statusCode() != 200) throw new PageNotFoundException();

            return rewriteApiResponse(apiResponseDto);
        } catch (PageNotFoundException e) {
            throw new PageNotFoundException();
        } catch (Exception e) {
            log.error("Erro ao buscar personagens: " + e.getMessage(), e);
        }
        return null;
    }

    public ResponseEntity<byte[]> findCharacterAvatar(String id) {
        if (!id.matches("\\d+")) throw new InvalidIdException();
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(config.getApiBaseUrl() + "/character/" + id))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 404) throw new CharacterNotFoundException();

            ObjectMapper objectMapper = new ObjectMapper();
            String imageUrl = objectMapper.readTree(response.body()).get("image").asText();
            byte[] imageBytes = downloadImage(URI.create(imageUrl).toURL());

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "image/jpeg");

            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        }catch (CharacterNotFoundException e) {
            throw new CharacterNotFoundException();
        }catch (Exception e) {
            log.error("Erro ao buscar avatar do personagem: " + e.getMessage(), e);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public CharacterDto findACharacterById(String id) {
        if (!id.matches("\\d+")) {
            throw new InvalidIdException();
        }
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(config.getApiBaseUrl() + "/character/" + id))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper();

            if (response.statusCode() == 404) throw new CharacterNotFoundException();

            CharacterDto character = objectMapper.readValue(response.body(), CharacterDto.class);
            return rewriteCharacterDto(character);
        } catch (CharacterNotFoundException e) {
            throw new CharacterNotFoundException();
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