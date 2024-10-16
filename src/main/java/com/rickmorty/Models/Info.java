package com.rickmorty.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Info {
    @JsonProperty("count")
    private int count;

    @JsonProperty("pages")
    private int pages;

    @JsonProperty("next")
    private String next;

    @JsonProperty("prev")
    private String prev;
}