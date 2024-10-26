package com.rickmorty.Models;

import java.util.List;
import lombok.Data;

@Data
public class LocationModel {

    private int id;

    private String name;

    private String type;

    private String dimension;

    private List<String> residents;

    private String url;

}
