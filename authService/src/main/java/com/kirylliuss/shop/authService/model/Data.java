package com.kirylliuss.shop.authService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "auth_data")
@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class Data {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login")
    private String login;

    @Column(name = "hashed_password")
    private String hashedPassword;

    @Column(name = "access")
    private String access;

    public Data(String login, String hashedPassword, String access) {
        this.login = login;
        this.hashedPassword = hashedPassword;
        this.access = access;
    }
}
