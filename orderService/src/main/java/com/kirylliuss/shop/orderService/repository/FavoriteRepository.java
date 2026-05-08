package com.kirylliuss.shop.orderService.repository;

import com.kirylliuss.shop.orderService.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Long, Favorite> {
    List<Favorite> findAllByUserId(Long userId);
    void deleteByUserIdAndItemId(Long userId, Long itemId);
    boolean existsByUserIdAndItemId(Long userId, Long itemId);
}
