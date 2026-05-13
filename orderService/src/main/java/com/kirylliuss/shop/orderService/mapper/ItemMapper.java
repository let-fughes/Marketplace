package com.kirylliuss.shop.orderService.mapper;

import com.kirylliuss.shop.orderService.dto.request.ItemRequest;
import com.kirylliuss.shop.orderService.dto.response.ItemResponse;
import com.kirylliuss.shop.orderService.model.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(target = "imageUrl", ignore = true)
    Item toItem(ItemRequest itemRequest);

    ItemResponse toItemResponse(Item item);

    @Mapping(target = "id", ignore = true)
    void updateItemFromRequest(ItemRequest request, @MappingTarget Item entity);
}
