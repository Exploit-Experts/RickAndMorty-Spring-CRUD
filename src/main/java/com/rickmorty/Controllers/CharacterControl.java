package com.rickmorty.Controllers;

import com.rickmorty.Services.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import com.rickmorty.Models.CharacterModel;


@RestController
@RequestMapping("/character")
public class CharacterControl {

    @Autowired
    private CharacterService characterService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<CharacterModel> getCharacterById(@PathVariable String id) {
        return characterService.findACharacterById(id);
    }
}