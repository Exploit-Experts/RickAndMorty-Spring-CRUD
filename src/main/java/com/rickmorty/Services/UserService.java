package com.rickmorty.Services;

import com.rickmorty.DTO.UserDto;
import com.rickmorty.Models.UserModel;
import com.rickmorty.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    // Salva um novo usuário
    public UserModel saveUser(UserDto userDto) {
        UserModel userModel = new UserModel(userDto.name(), userDto.surname(), userDto.email(), userDto.password());
        return userRepository.save(userModel);
    }
}