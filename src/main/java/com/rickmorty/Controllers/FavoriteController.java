package com.rickmorty.Controllers;

import com.rickmorty.Services.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.rickmorty.DTO.FavoriteDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @PostMapping
    public ResponseEntity<Void> createFavorite(@RequestBody FavoriteDto favoriteDto){
        favoriteService.create(favoriteDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{favoriteId}/users/{userId}")
    public ResponseEntity<Void> removeFavorite(@PathVariable Long favoriteId, @PathVariable Long userId) {
        favoriteService.removeFavorite(userId, favoriteId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
