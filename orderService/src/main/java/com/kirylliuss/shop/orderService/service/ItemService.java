package com.kirylliuss.shop.orderService.service;

import com.kirylliuss.shop.orderService.client.UserServiceClient;
import com.kirylliuss.shop.orderService.dto.request.FavoriteRequest;
import com.kirylliuss.shop.orderService.dto.request.ItemRequest;
import com.kirylliuss.shop.orderService.dto.response.FavoriteResponse;
import com.kirylliuss.shop.orderService.dto.response.ItemResponse;
import com.kirylliuss.shop.orderService.dto.response.UserResponse;
import com.kirylliuss.shop.orderService.mapper.FavoriteMapper;
import com.kirylliuss.shop.orderService.mapper.ItemMapper;
import com.kirylliuss.shop.orderService.model.Favorite;
import com.kirylliuss.shop.orderService.model.Item;
import com.kirylliuss.shop.orderService.repository.FavoriteRepository;
import com.kirylliuss.shop.orderService.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    private final FavoriteMapper favoriteMapper;

    private final ItemMapper itemMapper;

    private final ImageService imageService;

    private final FavoriteRepository favoriteRepository;

    private final UserServiceClient client;

    @Transactional
    public List<ItemResponse> getAllItems(){
        return itemRepository.findAll().stream()
                .map(itemMapper::toItemResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ItemResponse> getItemsByCategory(String category){
        return itemRepository.findByCategory(category).stream()
                .map(itemMapper::toItemResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ItemResponse createItem(ItemRequest request, MultipartFile file, Long userId) {
        String fileUrl = imageService.uploadImage(file);

        Item item = itemMapper.toItem(request);

        item.setImageUrl(fileUrl);
        item.setUserId(userId);

        Item savedItem = itemRepository.save(item);

        return itemMapper.toItemResponse(savedItem);
    }

    @Transactional(readOnly = true)
    public ItemResponse getItemById(Long id){
        Item item = itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        return itemMapper.toItemResponse(item);
    }

    @Transactional(readOnly = true)
    public ItemResponse getItemByName(String name) {
        Item item = itemRepository.findByName(name);
        if (item == null) {
            throw new RuntimeException("Item not found with name: " + name);
        }
        return itemMapper.toItemResponse(item);
    }

    @Transactional
    public ItemResponse updateItem(ItemRequest request, Long id){
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));

        itemMapper.updateItemFromRequest(request, item);
        Item updatedItem = itemRepository.save(item);
        return itemMapper.toItemResponse(updatedItem);
    }

    @Transactional
    public void deleteItem(Long id){
        if(!itemRepository.existsById(id)){
            throw new RuntimeException("Not found!");
        }
        itemRepository.deleteById(id);
    }

    @Transactional
    public FavoriteResponse addToFavorite(FavoriteRequest request){
        if(itemRepository.existsById(request.getItemId())){
            Favorite favorite = favoriteRepository.save(favoriteMapper.toFavorite(request));
            return favoriteMapper.toFavoriteResponse(favorite);
        } else {
            throw new RuntimeException("Item not found.");
        }
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> getFavoritesByUserId(Long userId){
        return favoriteRepository.findAllByUserId(userId).stream()
                .map(favoriteMapper::toFavoriteResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFromFavorites(FavoriteRequest request){
        favoriteRepository.deleteByUserIdAndItemId(request.getUserId(), request.getItemId() );
    }

    public UserResponse getUserByItemId(Long itemId){
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Item not found."));
        ItemResponse response = itemMapper.toItemResponse(item);
        return client.getUserById(response.getUserId());
    }
}
