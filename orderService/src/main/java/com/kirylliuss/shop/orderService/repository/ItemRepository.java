package com.kirylliuss.shop.orderService.repository;

import com.kirylliuss.shop.orderService.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Item findByName(String name);

    List<Item> findByNameContainingIgnoreCase(String name);
}
