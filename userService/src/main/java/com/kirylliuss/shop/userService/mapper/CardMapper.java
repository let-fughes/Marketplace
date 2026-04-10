package com.kirylliuss.shop.userService.mapper;

import com.kirylliuss.shop.userService.dto.request.CardRequest;
import com.kirylliuss.shop.userService.dto.response.CardResponse;
import com.kirylliuss.shop.userService.model.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface CardMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Card toCard(CardRequest request);

    @Mapping(target = "userId", source = "user.id")
    CardResponse toCardResponse(Card card);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateCardFromCardRequest(@MappingTarget Card card, CardRequest request);
}
