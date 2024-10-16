package com.rickmorty.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationResponse {

    @JsonProperty("info")
    private Info info;

    @JsonProperty("results")
    private List<LocationModel> results;

    public Info getInfo() { return info; }
    public void setInfo(Info info) { this.info = info; }

    public List<LocationModel> getResults() { return results; }
    public void setResults(List<LocationModel> results) { this.results = results; }
}
