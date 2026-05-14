package com.kirylliuss.shop.orderService.controller;

import com.kirylliuss.shop.orderService.client.UserServiceClient;
import com.kirylliuss.shop.orderService.dto.request.FavoriteRequest;
import com.kirylliuss.shop.orderService.dto.request.ItemRequest;
import com.kirylliuss.shop.orderService.dto.response.FavoriteResponse;
import com.kirylliuss.shop.orderService.dto.response.ImageUrlResponse;
import com.kirylliuss.shop.orderService.dto.response.ItemResponse;
import com.kirylliuss.shop.orderService.dto.response.UserResponse;
import com.kirylliuss.shop.orderService.mapper.FavoriteMapper;
import com.kirylliuss.shop.orderService.model.Favorite;
import com.kirylliuss.shop.orderService.service.ImageService;
import com.kirylliuss.shop.orderService.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/items")
@Validated
public class ItemController {

    private final ItemService itemService;
    private final ImageService imageService;

    private final FavoriteMapper favoriteMapper;

    private final UserServiceClient client;

    @GetMapping
    public ResponseEntity<List<ItemResponse>> getAllItems(){
        List<ItemResponse> responses = itemService.getAllItems();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ItemResponse> getItemById(@PathVariable Long id) {
        ItemResponse item = itemService.getItemById(id);
        return ResponseEntity.ok(item);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ItemResponse> getItemByName(@PathVariable String name) {
        ItemResponse itemResponse = itemService.getItemByName(name);
        return ResponseEntity.ok(itemResponse);
    }

    @GetMapping("/getImage/{id}")
    public ResponseEntity<ImageUrlResponse> getImageURL(@PathVariable Long id){
        ImageUrlResponse response = new ImageUrlResponse();
        response.setUrl(imageService.getImage(id));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/getByCategory/{category}")
    public ResponseEntity<List<ItemResponse>> getItemByCategory(@PathVariable String category){
        List<ItemResponse> responses = itemService.getItemsByCategory(category);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/getFavorites/{userId}")
    public ResponseEntity<List<FavoriteResponse>> getFavoritesByUserId(@PathVariable Long userId){
        List<FavoriteResponse> list = itemService.getFavoritesByUserId(userId);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/addToFavorites")
    public ResponseEntity<FavoriteResponse> addToFavorites(@Valid @RequestBody FavoriteRequest request){
        FavoriteResponse response = itemService.addToFavorite(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ItemResponse> createItem(
            @RequestPart("item") @Valid ItemRequest request,
            @RequestPart("file") MultipartFile file,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserResponse userResponse = client.getProfile(principal.getName());

        System.out.println("DEBUG: User profile from User Service: " + userResponse);
        if (userResponse != null) {
            System.out.println("DEBUG: User ID found: " + userResponse.getId());
        }

        ItemResponse response = itemService.createItem(request, file, userResponse.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemResponse> updateItem(@Valid @RequestBody ItemRequest request, @PathVariable Long id) {
        ItemResponse item = itemService.updateItem(request, id);
        return ResponseEntity.ok(item);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ItemResponse> deleteItem(@PathVariable Long id) {
        ItemResponse response = itemService.getItemById(id);
        itemService.deleteItem(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/deleteFromFavorites")
    public ResponseEntity<FavoriteResponse> deleteFromFavorites(@RequestBody FavoriteRequest request){
        Favorite favorite = favoriteMapper.toFavorite(request);
        itemService.deleteFromFavorites(request);
        FavoriteResponse response =  favoriteMapper.toFavoriteResponse(favorite);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/getUserByItemId/{id}")
    public ResponseEntity<UserResponse> getUserByItemId(@PathVariable("id") Long itemId){
        UserResponse response = itemService.getUserByItemId(itemId);
        return ResponseEntity.ok(response);
    }


}