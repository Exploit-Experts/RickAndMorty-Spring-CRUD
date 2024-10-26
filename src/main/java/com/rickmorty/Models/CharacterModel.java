package com.rickmorty.Models;

import lombok.Data;
import java.util.List;

@Data
public class CharacterModel {
    
    private int id;

    private String name;

    private String status;

    private String species;

    private String type;

    private String gender;

    private String image;

    private List<String> episode;

}