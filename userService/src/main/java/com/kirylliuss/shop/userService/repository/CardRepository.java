package com.kirylliuss.shop.userService.repository;

import com.kirylliuss.shop.userService.model.Card;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card,Long> {
    List<Card> findByUserId(Long userId);

    List<Card> findByIdIn(List<Long> ids);

    @Modifying
    @Transactional
    @Query(
            "UPDATE Card c SET c.number = :number, c.holder = :holder, c.expirationDate = :expirationDate WHERE c.id = :id")
    void updateCardById(
            @Param("id") Long id,
            @Param("number") String number,
            @Param("expirationDate") LocalDate expirationDate,
            @Param("holder") String holder);
}
