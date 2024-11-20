package com.rickmorty.Services;
import com.rickmorty.DTO.FavoriteResponseDto;
import com.rickmorty.Models.FavoriteModel;
import com.rickmorty.Models.UserModel;
import com.rickmorty.Repository.FavoriteRepository;
import com.rickmorty.Repository.UserRepository;
import com.rickmorty.exceptions.*;
import com.rickmorty.exceptions.InvalidParameterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rickmorty.DTO.FavoriteDto;
import com.rickmorty.exceptions.UserNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import java.util.*;

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
    public void create(FavoriteDto favoriteDto) {
        FavoriteModel favorite;
        if (favoriteDto.userId() == null || favoriteDto.userId() <= 0 ||
                favoriteDto.apiId() == null || favoriteDto.apiId() <= 0) throw new InvalidIdException();

        switch (favoriteDto.itemType().name().toLowerCase()){
            case "episode":
                if (episodeService.findEpisodeById(String.valueOf(favoriteDto.apiId())) == null) throw new EpisodeNotFoundException();
                break;
            case "character":
                if (characterService.findACharacterById(favoriteDto.apiId()) == null) throw new CharacterNotFoundException();
                break;
            case "location":
                if (locationService.getLocationById(favoriteDto.apiId()) == null) throw new LocationNotFoundException("Location não encontrado para o ID");
                break;
        }

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
            if (existsFavoriteAndUserSetted == 0) {
                favoriteRepository.addFavoriteToUser(user.getId(), favorite.getId());
            }
        }catch (NumberFormatException e){
            throw new InvalidIdException();
        }
    }

    public List<FavoriteResponseDto> getAllFavorites(Long userId) {
        Optional<List<FavoriteModel>> favorites = favoriteRepository.findFavoriteByUserId(userId);

        if (favorites.isPresent()) {
            List<FavoriteResponseDto> favoriteList = new ArrayList<>();
            favorites.get()
                    .forEach(favoriteModel ->
                            favoriteList.add(new FavoriteResponseDto(
                                    favoriteModel.getId(),
                                    favoriteModel.getApiId(),
                                    favoriteModel.getItemType(),
                                    userId
                            ))
                    );
            return favoriteList;
        }
        return null;
    }

    @Transactional
    public void removeFavorite(Long userId, Long favoriteId) {
        if (userId == null || userId < 1 || favoriteId == null || favoriteId < 1) throw new InvalidParameterException("Parâmetro useId e/ou favoriteId inválido");
        favoriteRepository.deleteByUserIdAndFavoriteId(userId, favoriteId);
    }

    @Transactional
    public void removeAllFavoritesByUserId(Long userId) {
        Optional<UserModel> user = userRepository.findByIdAndActive(userId, 1);
        if (user.isEmpty()){
            throw new UserNotFoundException();
        }

        favoriteRepository.deleteAllByUserId(userId);
    }
}