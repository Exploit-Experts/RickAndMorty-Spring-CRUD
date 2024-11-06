package com.rickmorty.Services;

import com.rickmorty.DTO.UserDto;
import com.rickmorty.Models.UserModel;
import com.rickmorty.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public void saveUser(UserDto userDto) {
        UserModel userModel = new UserModel(userDto.name(), userDto.surname(), userDto.email(), userDto.password());
        userRepository.save(userModel);
    }

    public void updateUser(Long id, UserDto userDto) {
        Optional<UserModel> optionalUser = userRepository.findByIdAndActive(id, 1);

        if (optionalUser.isPresent()) {
            UserModel user = optionalUser.get();

            user.setName(userDto.name());
            user.setSurname(userDto.surname());
            user.setEmail(userDto.email());
            user.setPassword(userDto.password());
            user.setDate_update(LocalDateTime.now());

            userRepository.save(user);
        }
    }
}