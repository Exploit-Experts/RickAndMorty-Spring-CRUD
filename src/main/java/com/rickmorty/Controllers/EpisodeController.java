package com.rickmorty.Controllers;


import com.rickmorty.DTO.EpisodeDto;
import com.rickmorty.DTO.LocationDto;
import com.rickmorty.Services.EpisodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/episodes")
public class EpisodeController {

    @Autowired
    private EpisodeService episodeService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EpisodeDto> getAllLocations() {
        return episodeService.findAllEpisode();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EpisodeDto getEpisodeById(@PathVariable String id) {
        return episodeService.getEpisodeById(id);
    }
}