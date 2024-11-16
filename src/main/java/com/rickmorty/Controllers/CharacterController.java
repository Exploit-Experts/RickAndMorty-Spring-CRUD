package com.rickmorty.Controllers;

import com.rickmorty.Services.CharacterService;
import com.rickmorty.DTO.ApiResponseDto;
import com.rickmorty.DTO.CharacterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/characters")
public class CharacterController {

    @Autowired
    private CharacterService characterService;

    @GetMapping
    public ResponseEntity<ApiResponseDto<CharacterDto>> getAllCharacters(
        @RequestParam(required = false) Integer page) {
        ApiResponseDto<CharacterDto> characters = characterService.findAllCharacters(page);
        return ResponseEntity.ok(characters);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CharacterDto> getCharacterById(@PathVariable Long id) {
        CharacterDto character = characterService.findACharacterById(id);
        return new ResponseEntity<>(character, HttpStatus.OK);
    }

    @GetMapping("/avatar/{id}.jpeg")
    public ResponseEntity<byte[]> getCharacterAvatar(@PathVariable Long id) {
        return characterService.findCharacterAvatar(id);
    }
}
