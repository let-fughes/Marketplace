package com.kirylliuss.shop.orderService.mapper;

import com.kirylliuss.shop.orderService.dto.request.FavoriteRequest;
import com.kirylliuss.shop.orderService.dto.response.FavoriteResponse;
import com.kirylliuss.shop.orderService.model.Favorite;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FavoriteMapper {
    FavoriteResponse toFavoriteResponse(Favorite favorite);
    Favorite toFavorite(FavoriteRequest request);
}
