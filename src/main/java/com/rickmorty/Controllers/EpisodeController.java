package com.rickmorty.Controllers;


import com.rickmorty.DTO.EpisodeDto;
import com.rickmorty.Services.EpisodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/episode")
public class EpisodeController {

    @Autowired
    private EpisodeService episodeService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EpisodeDto getEpisodeById(@PathVariable String id) {
        return episodeService.getEpisodeById(id);
    }
}