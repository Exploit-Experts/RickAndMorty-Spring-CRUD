package com.rickmorty.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rickmorty.DTO.FavoriteDto;
import com.rickmorty.Services.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
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
}
