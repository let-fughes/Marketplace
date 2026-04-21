package com.kirylliuss.shop.orderService.service;

import com.kirylliuss.shop.orderService.client.UserServiceClient;
import com.kirylliuss.shop.orderService.dto.request.ItemRequest;
import com.kirylliuss.shop.orderService.dto.request.OrderItemRequest;
import com.kirylliuss.shop.orderService.dto.request.OrderRequest;
import com.kirylliuss.shop.orderService.dto.response.ItemResponse;
import com.kirylliuss.shop.orderService.dto.response.OrderItemResponse;
import com.kirylliuss.shop.orderService.dto.response.OrderResponse;
import com.kirylliuss.shop.orderService.dto.response.UserResponse;
import com.kirylliuss.shop.orderService.exception.UserNotFoundException;
import com.kirylliuss.shop.orderService.mapper.ItemMapper;
import com.kirylliuss.shop.orderService.mapper.OrderMapper;
import com.kirylliuss.shop.orderService.model.Item;
import com.kirylliuss.shop.orderService.model.Order;
import com.kirylliuss.shop.orderService.model.OrderItem;
import com.kirylliuss.shop.orderService.repository.ItemRepository;
import com.kirylliuss.shop.orderService.repository.OrderItemRepository;
import com.kirylliuss.shop.orderService.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final OrderItemRepository orderItemRepository;

    private final OrderMapper orderMapper;
    private final ItemMapper itemMapper;

    private final UserServiceClient userServiceClient;

    private final ImageService imageService;

    @Transactional
    public List<ItemResponse> getAllItems(){
        return itemRepository.findAll().stream()
                .map(itemMapper::toItemResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ItemResponse createItem(ItemRequest request, MultipartFile file){

        String fileUrl = imageService.uploadImage(file);

        Item item = itemMapper.toItem(request);
        item.setImageUrl(fileUrl);

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
    public OrderItemResponse addToCard(OrderItemRequest request){
        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + request.getItemId()));
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setQuantity(request.getQuantity());

        orderItemRepository.save(orderItem);

        return orderMapper.toOrderItemResponse(orderItem);
    }

    @Transactional
    public void deleteFromCard(Long id){
        if (!orderItemRepository.existsById(id)) {
            throw new RuntimeException("OrderItem not found with id: " + id);
        }
        orderItemRepository.deleteById(id);
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {

        UserResponse user = userServiceClient.getUserById(request.getUserId());

        if (user == null) {
            throw new UserNotFoundException("User not found in UserService", HttpStatus.NOT_FOUND);
        }

        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setStatus("CREATED");

        Order savedOrder = orderRepository.save(order);

        List<OrderItem> orderItems = request.getItems().stream()
                .map(itemRequest -> {
                    Item item = itemRepository.findById(itemRequest.getItemId())
                            .orElseThrow(() -> new RuntimeException("Item not found: " + itemRequest.getItemId()));

                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(savedOrder);
                    orderItem.setItem(item);
                    orderItem.setQuantity(itemRequest.getQuantity());
                    return orderItem;
                })
                .collect(Collectors.toList());

        orderItemRepository.saveAll(orderItems);
        savedOrder.setOrderItems(orderItems);

        return orderMapper.toResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return orderMapper.toResponse(order);
    }

    @Transactional
    public void deleteOrder(Long id){
        if(!orderRepository.existsById(id)){
            throw new RuntimeException("Not Found!");
        }
        orderRepository.deleteById(id);
    }
}