package com.rickmorty.Utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Config {
    @Value("${api.base.url}")
    private String apiBaseUrl;
    @Value("${local.base.url}")
    private String localBaseUrl;

    public String getApiBaseUrl() {
        return apiBaseUrl;
    }
    public String getLocalBaseUrl() {
        return localBaseUrl;
    }
}
