package com.rickmorty.Services;

import com.rickmorty.DTO.UserDto;
import com.rickmorty.Models.UserModel;
import com.rickmorty.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public UserModel saveUser(UserDto userDto) {
        UserModel userModel = new UserModel(userDto.name(), userDto.surname(), userDto.email(), userDto.password());
        return userRepository.save(userModel);
    }

    public void deleteUser(Long id) {
        UserModel userModel = userRepository.findById(id).orElse(null);
        if (userModel != null || userModel.getActive() != 0) {
            userModel.setActive(0);
            userModel.setDeleted_at(LocalDate.now());
            userRepository.save(userModel);
        }
    }
}