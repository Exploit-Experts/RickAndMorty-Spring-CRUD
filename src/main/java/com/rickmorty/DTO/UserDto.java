package com.rickmorty.DTO;

public record UserDto(
        String name,
        String surname,
        String email,
        String password
) {

}