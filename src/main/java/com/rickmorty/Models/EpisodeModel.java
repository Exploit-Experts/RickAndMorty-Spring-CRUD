
package com.rickmorty.Models;

import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class EpisodeModel {

    private int id;

    private String name;

    private String episodeCode;

    private Date releaseDate;

    private List<String> characters;




}