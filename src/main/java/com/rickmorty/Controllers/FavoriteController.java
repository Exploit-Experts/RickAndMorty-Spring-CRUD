package com.rickmorty.Controllers;

import com.rickmorty.Models.FavoriteModel;
import com.rickmorty.enums.ItemType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rickmorty.DTO.FavoriteDto;
import com.rickmorty.Services.FavoriteService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private final com.rickmorty.Controllers.FavoriteRepository favoriteRepository;
    private final FavoriteService favoriteService;

    @Autowired
    public <FavoriteRepository> FavoriteController(FavoriteRepository favoriteRepository, FavoriteService favoriteService) {
        this.favoriteRepository = favoriteRepository;
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<Void> createFavorite(@RequestBody FavoriteDto favoriteDto) {
        favoriteService.create(favoriteDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<FavoriteModel>> getFavoritesByUserId(@PathVariable Long userId) {
        Optional<List<FavoriteModel>> favoritesOpt = favoriteRepository.findFavoriteByUserId(userId);

        if (favoritesOpt.isPresent() && !favoritesOpt.get().isEmpty()) {
            return ResponseEntity.ok(favoritesOpt.get());
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/item")
    public ResponseEntity<FavoriteModel> getFavoriteByApiIdAndItemType(
            @RequestParam Long apiId,
            @RequestParam String itemType) {

        try {
            ItemType type = ItemType.valueOf(itemType.toUpperCase());
            Optional<FavoriteModel> favoriteOpt = favoriteRepository.findByApiIdAndItemType(apiId, type);

            return favoriteOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Novo endpoint para obter favorito por usuário e episódio
    @GetMapping("/user/{userId}/episode/{episodeId}")
    public ResponseEntity<FavoriteModel> getFavoriteByUserAndEpisode(
            @PathVariable Long userId,
            @PathVariable Long episodeId) {

        Optional<FavoriteModel> favoriteOpt = favoriteRepository.findByUserIdAndEpisodeId(userId, episodeId);

        return favoriteOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}

