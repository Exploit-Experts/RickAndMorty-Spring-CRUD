package com.rickmorty.Services;
import com.rickmorty.Models.FavoriteModel;
import com.rickmorty.Models.UserModel;
import com.rickmorty.Repository.FavoriteRepository;
import com.rickmorty.Repository.UserRepository;
import com.rickmorty.exceptions.InvalidParameterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rickmorty.DTO.FavoriteDto;
import com.rickmorty.exceptions.UserNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class FavoriteService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Transactional
    public void create(FavoriteDto favoriteDto) {
        FavoriteModel favorite;
        UserModel user = userRepository.findById(favoriteDto.userId())
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
    }

    public List<FavoriteDto> getAllFavorites(Long userId) {
        Optional<List<FavoriteModel>> favorites = favoriteRepository.findFavoriteByUserId(userId);

        if (favorites.isPresent()) {
            List<FavoriteDto> favoriteList = new ArrayList<>();
            favorites.get()
                    .forEach(favoriteModel ->
                            favoriteList.add(new FavoriteDto(
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
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException();
        }

        favoriteRepository.deleteAllByUserId(userId);
    }
}