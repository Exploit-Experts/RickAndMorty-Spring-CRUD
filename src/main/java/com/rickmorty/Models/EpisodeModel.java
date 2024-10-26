
package com.rickmorty.Models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

public class EpisodeModel {

    private int id;

    private String name;

    private String episodeCode;

    private Date releaseDate;

    private List<String> characters;




}