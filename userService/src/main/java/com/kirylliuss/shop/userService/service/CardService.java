package com.kirylliuss.shop.userService.service;

import com.kirylliuss.shop.userService.dto.request.CardRequest;
import com.kirylliuss.shop.userService.dto.response.CardResponse;
import com.kirylliuss.shop.userService.exceptions.CardNotFoundException;
import com.kirylliuss.shop.userService.mapper.CardMapper;
import com.kirylliuss.shop.userService.model.Card;
import com.kirylliuss.shop.userService.repository.CardRepository;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    @Cacheable(value = "CardsByIds", key = "#user_id")
    @Transactional(readOnly = true)
    public List<CardResponse> getCardsByIdIn(List<Long> ids) {
        return cardRepository.findByIdIn(ids).stream()
                .map(cardMapper::toCardResponse)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "cardById", key = "#id")
    @Transactional(readOnly = true)
    public CardResponse getCardById(Long id) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id));
        return cardMapper.toCardResponse(card);
    }

    @Caching(evict = {@CacheEvict(value = "createCard", allEntries = true)})
    @Transactional
    public CardResponse createCard(@Valid CardRequest cardRequest) throws ValidationException {
        Card card = cardMapper.toCard(cardRequest);
        Card savedCard = cardRepository.save(card);
        return cardMapper.toCardResponse(savedCard);
    }

    @Caching(
            evict = {
                    @CacheEvict(
                            value = "userCards",
                            key = "#result.userId",
                            condition = "#result != null"),
                    @CacheEvict(value = "cards", key = "#id")
            })
    @Transactional
    public CardResponse updateCard(Long id, @Valid CardRequest cardRequest) {
        Card existingCard =
                cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id));

        cardMapper.updateCardFromCardRequest(existingCard, cardRequest);
        Card updatedCard = cardRepository.save(existingCard);

        return cardMapper.toCardResponse(updatedCard);
    }

    @Caching(evict = {@CacheEvict(value = "deleteCards", key = "#id")})
    @Transactional
    public void deleteCard(Long id) {
        if (!cardRepository.existsById(id)) {
            throw new CardNotFoundException(id);
        }
        cardRepository.deleteById(id);
    }
}
