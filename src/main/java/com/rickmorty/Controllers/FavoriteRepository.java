package com.rickmorty.Controllers;

import com.rickmorty.Models.FavoriteModel;
import com.rickmorty.enums.ItemType;

import java.util.List;
import java.util.Optional;

public class FavoriteRepository {
    public Optional<List<FavoriteModel>> findFavoriteByUserId(Long userId) {
    }

    public Optional<FavoriteModel> findByApiIdAndItemType(Long apiId, ItemType type) {
    }
}
