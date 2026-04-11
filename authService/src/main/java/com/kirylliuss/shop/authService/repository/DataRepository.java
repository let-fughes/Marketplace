package com.kirylliuss.shop.authService.Repository;

import com.kirylliuss.shop.authService.model.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface DataRepository extends JpaRepository<Data, Long> {

    Optional<Data> findByLogin(String login);

    boolean existsByLogin(String login);

    @Modifying
    @Query("UPDATE Data d SET d.hashedPassword = :hashedPassword WHERE d.login = :login")
    int updatePassword(
            @Param("login") String login,
            @Param("hashedPassword") String hashedPassword
    );

    @Transactional
    void deleteByLogin(String login);
}