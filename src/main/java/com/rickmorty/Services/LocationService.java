package com.rickmorty.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rickmorty.Models.CharacterModel;
import com.rickmorty.Models.LocationModel;
import com.rickmorty.Models.LocationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Service
public class LocationService {

    private final WebClient webClient;

    @Autowired
    public LocationService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://rickandmortyapi.com/api").build();
    }

    public Mono<LocationResponse> findAllLocations() {
        log.info("Buscando localizações");
        return webClient
                .get()
                .uri("/location")
                .accept(APPLICATION_JSON)
                .retrieve()
                .bodyToMono(LocationResponse.class)
                .doOnNext(location -> log.info("Localização recebida: {}", location))
                .doOnError(e -> log.error("Erro ao buscar localizações: {}", e.getMessage()));
    }

    public Mono<LocationModel> findLocationById(String id) {
        return webClient
                .get()
                .uri("/location/" + id)
                .accept(APPLICATION_JSON)
                .retrieve()
                .bodyToMono(LocationModel.class)
                .doOnError(e -> log.error("Erro ao buscar localização por ID {}: {}", id, e.getMessage()));
    }
}