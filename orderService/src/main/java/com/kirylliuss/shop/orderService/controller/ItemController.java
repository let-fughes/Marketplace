package com.kirylliuss.shop.orderService.controller;

import com.kirylliuss.shop.orderService.dto.request.ItemRequest;
import com.kirylliuss.shop.orderService.dto.response.ImageUrlResponse;
import com.kirylliuss.shop.orderService.dto.response.ItemResponse;
import com.kirylliuss.shop.orderService.service.ImageService;
import com.kirylliuss.shop.orderService.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/items")
@Validated
public class ItemController {

    private final ItemService itemService;
    private final ImageService imageService;

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

    @PostMapping
    public ResponseEntity<ItemResponse> createItem(
            @Valid @RequestPart("item") ItemRequest request,
            @RequestPart("file") MultipartFile file) {

        ItemResponse response = itemService.createItem(request, file);

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
}