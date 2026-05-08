package com.kirylliuss.shop.orderService.repository;

import com.kirylliuss.shop.orderService.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Long, Favorite> {
    List<Favorite> findAllByUserId(Long userId);
    void deleteByUserIdAndItemId(Long userId, Long itemId);
    boolean existsByUserIdAndItemId(Long userId, Long itemId);
}
