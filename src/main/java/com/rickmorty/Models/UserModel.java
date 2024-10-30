package com.rickmorty.Models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "users")
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean active = true;

    @Column(nullable = false)
    private String name;

    private String surname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private LocalDate date_register;

    private LocalDate date_update;

    private LocalDate deleted_at;

    public UserModel() {
    }

    public UserModel(Long id, String name, String surname, String email, String password) {
        this.id = id;
        this.active = true;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.date_register = LocalDate.now();
        this.date_update = LocalDate.now();
        this.deleted_at = null;
    }

    // Getters e Setters

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getDate_register() {
        return date_register;
    }

    public void setDate_register(LocalDate date_register) {
        this.date_register = date_register;
    }

    public LocalDate getDate_update() {
        return date_update;
    }

    public void setDate_update(LocalDate date_update) { // Alterado para setDate_update
        this.date_update = date_update;
    }

    public LocalDate getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(LocalDate deleted_at) {
        this.deleted_at = deleted_at;
    }

}
