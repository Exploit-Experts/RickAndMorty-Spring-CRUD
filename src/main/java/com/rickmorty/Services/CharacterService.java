package com.rickmorty.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rickmorty.Models.CharacterModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Service
public class CharacterService {

    private final WebClient webClient;

    @Autowired
    public CharacterService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://rickandmortyapi.com/api").build();
    }

    public Mono<CharacterModel> findACharacterById(String id) {
        log.info("Buscando personagem com o id [{}]", id);
        return webClient
                .get()
                .uri("/character/" + id)
                .accept(APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> log.info("Resposta da API: {}", response))
                .flatMap(response -> {
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        CharacterModel characterModel = objectMapper.readValue(response, CharacterModel.class);
                        return Mono.just(characterModel);
                    } catch (JsonProcessingException e) {
                        log.error("Erro ao deserializar a resposta: {}", e.getMessage());
                        return Mono.error(e);
                    }
                });
    }
}