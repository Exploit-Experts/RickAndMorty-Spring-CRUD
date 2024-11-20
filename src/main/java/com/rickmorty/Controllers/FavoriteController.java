package com.rickmorty.Controllers;

import com.rickmorty.Models.FavoriteModel;
import com.rickmorty.enums.ItemType;
import org.springframework.beans.factory.annotation.Autowired;
import com.rickmorty.Services.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.rickmorty.DTO.FavoriteDto;
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

    private final FavoriteService favoriteService;

    @Autowired
    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<Void> createFavorite(@RequestBody FavoriteDto favoriteDto) {
        favoriteService.create(favoriteDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @GetMapping("/{userId}")
    public ResponseEntity<List<FavoriteDto>> getAllFavoritesByUserId(@PathVariable Long userId) {
        List<FavoriteDto> favorites = favoriteService.getAllFavorites(userId);
        return new ResponseEntity<>(favorites, HttpStatus.OK);
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