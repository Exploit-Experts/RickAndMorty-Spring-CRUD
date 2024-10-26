package com.rickmorty.Models;

import jakarta.persistence.*;

import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "users")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean active;

    @Column(nullable = false)
    private String name;

    private String surname;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private LocalDate date_register;

    private LocalDate date_update;

    private LocalDate deleted_at;

    public UserModel() {}
   
}