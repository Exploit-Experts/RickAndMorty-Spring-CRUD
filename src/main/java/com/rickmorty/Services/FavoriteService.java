package com.rickmorty.Services;
import com.rickmorty.DTO.FavoriteResponseDto;
import com.rickmorty.Models.FavoriteModel;
import com.rickmorty.Models.UserModel;
import com.rickmorty.Repository.FavoriteRepository;
import com.rickmorty.Repository.UserRepository;
import com.rickmorty.enums.SortFavoriteField;
import com.rickmorty.exceptions.*;
import com.rickmorty.exceptions.InvalidParameterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.rickmorty.DTO.FavoriteDto;
import com.rickmorty.exceptions.UserNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    EpisodeService episodeService;

    @Autowired
    CharacterService characterService;

    @Autowired
    LocationService locationService;

    @Transactional
    public void create(FavoriteDto favoriteDto, BindingResult result) {
        validateFavorite(favoriteDto, result);

        FavoriteModel favorite;
        try {
            Long id = favoriteDto.userId();
            UserModel user = userRepository.findByIdAndActive(id, 1)
                    .orElseThrow(UserNotFoundException::new);

            Optional<FavoriteModel> favoriteOptional = favoriteRepository.findByApiIdAndItemType(favoriteDto.apiId(), favoriteDto.itemType());

            if (favoriteOptional.isEmpty()) {
                favorite = new FavoriteModel();
                favorite.setApiId(favoriteDto.apiId());
                favorite.setItemType(favoriteDto.itemType());

                favorite = favoriteRepository.save(favorite);
            } else {
                favorite = favoriteOptional.get();
            }

            Long existsFavoriteAndUserSetted = favoriteRepository.existsByUserIdAndFavoriteId(user.getId(), favorite.getId());
            if (existsFavoriteAndUserSetted != 0) throw new FavoriteAlreadyExists();

            favoriteRepository.addFavoriteToUser(user.getId(), favorite.getId());
        }catch (NumberFormatException e){
            throw new InvalidIdException();
        }
    }

    public Page<FavoriteResponseDto> getAllFavorites(Long userId, int page, int size, String[] sort) {
        String sortField = sort[0];
        if (!SortFavoriteField.isValid(sortField)) throw new InvalidParameterException("Campo sort inválido: " + sortField);

        Sort.Direction direction;
        try {
            direction = Sort.Direction.fromString(sort[1]);
        } catch (IllegalArgumentException e) {
            throw new InvalidParameterException("Direção de sort inválida: " + sort[1]);
        }

        Sort sortOrder = Sort.by(direction, sortField);
        Pageable pageable = PageRequest.of(page, size, sortOrder);

        Optional<UserModel> user = userRepository.findByIdAndActive(userId, 1);
        if (user.isEmpty()) throw new UserNotFoundException();

        Page<FavoriteModel> favoritesPage = favoriteRepository.findFavoriteByUserId(userId, pageable);
        if (favoritesPage.isEmpty()) throw new NotFoundException();

        return favoritesPage.map(favoriteModel -> new FavoriteResponseDto(
                favoriteModel.getId(),
                favoriteModel.getApiId(),
                favoriteModel.getItemType(),
                userId
        ));
    }


    @Transactional
    public void removeFavorite(Long userId, Long favoriteId) {
        if (userId == null || userId <= 0) throw new InvalidParameterException("Parâmetro userId inválido. Deve ser um número positivo maior que zero");
        if (favoriteId == null || favoriteId <= 0) throw new InvalidParameterException("Parâmetro favoriteId inválido. Deve ser um número positivo maior que zero");

        Optional<UserModel> user = userRepository.findByIdAndActive(userId, 1);
        if (user.isEmpty()) throw new UserNotFoundException();

        Long existsFavorite = favoriteRepository.existsByUserIdAndFavoriteId(userId, favoriteId);
        if (existsFavorite == 0) throw new FavoriteNotFound("Favorito não cadastrado");

        favoriteRepository.deleteByUserIdAndFavoriteId(userId, favoriteId);
    }

    @Transactional
    public void removeAllFavoritesByUserId(Long userId) {
        Optional<UserModel> user = userRepository.findByIdAndActive(userId, 1);
        if (user.isEmpty()) throw new UserNotFoundException();

        Long existsFavorite = favoriteRepository.existsByUserId(userId);
        if (existsFavorite == 0) throw new FavoriteNotFound("O usuário não tem favoritos cadastrados");

        favoriteRepository.deleteAllByUserId(userId);
    }

    private void validateFavorite(FavoriteDto favoriteDto, BindingResult result) {
        if (favoriteDto.userId() == null || favoriteDto.userId() <= 0) throw new InvalidParameterException("Parâmetro userId inválido. Deve ser um número positivo maior que zero");
        if (favoriteDto.apiId() == null || favoriteDto.apiId() <= 0) throw new InvalidParameterException("Parâmetro apiId inválido. Deve ser um número positivo maior que zero");

        switch (favoriteDto.itemType().name().toLowerCase()){
            case "episode":
                if (episodeService.findEpisodeById(favoriteDto.apiId()) == null) throw new EpisodeNotFoundException();
                break;
            case "character":
                if (characterService.findACharacterById(favoriteDto.apiId()) == null) throw new CharacterNotFoundException();
                break;
            case "location":
                if (locationService.getLocationById(favoriteDto.apiId()) == null) throw new LocationNotFoundException("Location não encontrado para o ID");
                break;
            default: throw new InvalidParameterException("itemType inválido. valores aceitos: (LOCATION, EPISODE, CHARACTER)");
        }

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            throw new ValidationErrorException(errors);
        }
    }
}