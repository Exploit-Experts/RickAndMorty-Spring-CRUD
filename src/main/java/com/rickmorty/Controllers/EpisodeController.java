package com.rickmorty.Controllers;

import com.rickmorty.DTO.ApiResponseDto;
import com.rickmorty.DTO.EpisodeDto;
import com.rickmorty.Services.EpisodeService;
import com.rickmorty.enums.SortEpisode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/episodes")
public class EpisodeController {

    @Autowired
    private EpisodeService episodeService;

    @GetMapping
    public ResponseEntity<ApiResponseDto<EpisodeDto>> getAllEpisodes(
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String episode,
        @RequestParam(required = false) SortEpisode sort) {
        ApiResponseDto<EpisodeDto> episodes = episodeService.findAllEpisodes(page, name, episode, sort);
        return ResponseEntity.ok(episodes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EpisodeDto> getEpisodeById(@PathVariable String id) {
        EpisodeDto episode = episodeService.findEpisodeById(id);
        return new ResponseEntity<>(episode, HttpStatus.OK);
    }
}