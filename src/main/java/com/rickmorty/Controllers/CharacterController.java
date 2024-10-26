package com.rickmorty.Controllers;

import com.rickmorty.Services.CharacterService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.rickmorty.DTO.CharacterDto;


@RestController
@RequestMapping("/characters")
public class CharacterController {

    @Autowired
    private CharacterService characterService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CharacterDto getCharacterById(@PathVariable String id) {
        return characterService.getCharacterById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CharacterDto> getAllCharacters() {
        return characterService.findAllCharacter();
    }
}