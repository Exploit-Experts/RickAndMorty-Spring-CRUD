package com.rickmorty.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rickmorty.DTO.EpisodeDto;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
@Service
public class EpisodeService {

    private static final String URL_API = "https://rickandmortyapi.com/api";

    public EpisodeDto getEpisodeById(String id) {
        System.out.println("Buscando episódio com o id [{}]" + id);
        EpisodeDto episodeDto = null;

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL_API + "/episode/" + id))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper();
            episodeDto = objectMapper.readValue(response.body(), EpisodeDto.class);

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        return episodeDto;
    }
    public EpisodeDto getAllEpisodes() {
        System.out.println("Buscando todos episódios ");
        EpisodeDto episodeDto = null;

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL_API + "/episode/" ))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper();
            episodeDto = objectMapper.readValue(response.body(), EpisodeDto.class);

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        return episodeDto;
    }
}