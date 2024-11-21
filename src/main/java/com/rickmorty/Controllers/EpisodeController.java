package com.rickmorty.Controllers;

import com.rickmorty.DTO.ApiResponseDto;
import com.rickmorty.DTO.EpisodeDto;
import com.rickmorty.Services.EpisodeService;
import com.rickmorty.enums.SortEpisode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;

@RestController
@RequestMapping("/api/v1/episodes")
public class EpisodeController {

    @Autowired
    private EpisodeService episodeService;

    @Operation(summary = "Get all episodes",
            description = "Get all episodes from the Rick and Morty series",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Episodes found"),
                    @ApiResponse(responseCode = "404", description = "Episodes not found", 
                                 content = @Content(mediaType = "application/json", 
                                                    examples = @ExampleObject(value = "{\"message\": \"Não encontrado\"}"))),
                    @ApiResponse(responseCode = "400", description = "Invalid parameter", 
                                 content = @Content(mediaType = "application/json", 
                                                    examples = @ExampleObject(value = "{\"message\": \"Parâmetro [] inválido\"}"))),
            })
    @GetMapping
    public ResponseEntity<ApiResponseDto<EpisodeDto>> getAllEpisodes(
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String episode,
        @RequestParam(required = false) SortEpisode sort) {
        ApiResponseDto<EpisodeDto> episodes = episodeService.findAllEpisodes(page, name, episode, sort);
        return ResponseEntity.ok(episodes);
    }

    @Operation(summary = "Get episode by ID",
            description = "Get a specific episode by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Episode found"),
                    @ApiResponse(responseCode = "404", description = "Episode not found", 
                                 content = @Content(mediaType = "application/json", 
                                                    examples = @ExampleObject(value = "{\"message\": \"Episode não encontrado para o ID\"}"))),
                    @ApiResponse(responseCode = "400", description = "Invalid episode ID", 
                                 content = @Content(mediaType = "application/json", 
                                                    examples = @ExampleObject(value = "{\"message\": \"Parâmetro id inválido\"}")))
            })
    @GetMapping("/{id}")
    public ResponseEntity<EpisodeDto> getEpisodeById(@PathVariable Long id) {
        EpisodeDto episode = episodeService.findEpisodeById(id);
        return new ResponseEntity<>(episode, HttpStatus.OK);
    }
}