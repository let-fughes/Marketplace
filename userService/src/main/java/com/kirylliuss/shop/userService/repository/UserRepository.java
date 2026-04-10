package com.kirylliuss.shop.userService.repository;

import com.kirylliuss.shop.userService.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    List<User> findByIdIn(List<Long> ids);

    Optional<User> findByEmail(String email);

    Optional<User> findByPhoneNumber(String phoneNumber);

    @Modifying
    @Transactional
    @Query(
            "UPDATE User u SET u.firstName = :name, u.lastName = :surname, u.birthDate = :birthDate, u.email = :email, u.phoneNumber = :phoneNumber WHERE u.id = :id")
    void updateUserById(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("surname") String surname,
            @Param("birthDate") LocalDate birthDate,
            @Param("email") String email,
            @Param("phoneNumber") String phoneNumber);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.cards WHERE u.id IN :ids")
    List<User> findByIdInWithCards(@Param("ids") List<Long> ids);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.cards WHERE u.id = :id")
    User findByIdWithCards(@Param("id") Long id);
}
