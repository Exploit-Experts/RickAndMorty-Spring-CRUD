package com.rickmorty.facades;

import com.rickmorty.dtos.ApiResponseDto;
import com.rickmorty.dtos.CharacterDto;
import com.rickmorty.services.CharacterService;
import com.rickmorty.enums.Gender;
import com.rickmorty.enums.LifeStatus;
import com.rickmorty.enums.SortOrder;
import com.rickmorty.enums.Species;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CharacterFacade {

    private final CharacterService characterService;

    public CharacterFacade(CharacterService characterService) {
        this.characterService = characterService;
    }

    public ApiResponseDto<CharacterDto> getAllCharacters(
            Integer page,
            String name,
            LifeStatus status,
            Species specie,
            String type,
            Gender gender,
            SortOrder order
    ) {
        return characterService.findAllCharacters(page, name, status, specie, type, gender, order);
    }

    public CharacterDto getCharacterById(Long id) {
        return characterService.findACharacterById(id);
    }

    public ResponseEntity<byte[]> getCharacterAvatar(Long id) {
        return characterService.findCharacterAvatar(id);
    }
}