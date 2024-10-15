package com.rickmorty.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class WebBuilder {
    private final WebClient webClient;

    public WebBuilder(WebClient.Builder builder) {
        webClient = builder.baseUrl("https://rickandmortyapi.com/api").build();
    }

    public WebClient getWebClient() {
        return webClient;
    }
}