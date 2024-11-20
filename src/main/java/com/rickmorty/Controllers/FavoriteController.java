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
@RequestMapping("/api/v1/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<Void> createFavorite(@RequestBody FavoriteDto favoriteDto){
        favoriteService.create(favoriteDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @GetMapping("/{userId}")
    public ResponseEntity<List<FavoriteDto>> getAllFavoritesByUserId(@PathVariable Long userId) {
        List<FavoriteDto> favorites = favoriteService.getAllFavorites(userId);
        return new ResponseEntity<>(favorites, HttpStatus.OK);
    }
}
