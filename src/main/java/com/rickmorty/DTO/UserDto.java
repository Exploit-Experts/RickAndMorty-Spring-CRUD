package com.rickmorty.DTO;

import java.time.LocalDate;

public record UserDto(String name,
                      String surname,
                      String email,
                      String password)
{}
