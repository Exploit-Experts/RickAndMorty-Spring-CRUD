package com.rickmorty.Controllers;

import com.rickmorty.DTO.FavoriteResponseDto;
import com.rickmorty.Services.FavoriteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.rickmorty.DTO.FavoriteDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<Void> createFavorite(@RequestBody FavoriteDto favoriteDto) {
        favoriteService.create(favoriteDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/favorites")
    public ResponseEntity<Page<FavoriteResponseDto>> getAllFavorites(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {

        Page<FavoriteResponseDto> favorites = favoriteService.getAllFavorites(userId, page, size, sort);

        return ResponseEntity.ok(favorites);
    }

    @DeleteMapping("/{userId}/{favoriteId}")
    public ResponseEntity<String> removeFavorite(@PathVariable Long userId, @PathVariable Long favoriteId) {
        favoriteService.removeFavorite(userId, favoriteId);
        return new ResponseEntity<>("Favorito removido com sucesso.", HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> removeFavoritesByUserId(@PathVariable Long userId) {
        favoriteService.removeAllFavoritesByUserId(userId);
        return new ResponseEntity<>("Todos os favoritos do usuário foram removidos com sucesso.", HttpStatus.NO_CONTENT);
    }
}