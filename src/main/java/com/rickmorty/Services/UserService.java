package com.rickmorty.Services;

import com.rickmorty.DTO.UserDto;
import com.rickmorty.DTO.UserPatchDto;
import com.rickmorty.Models.UserModel;
import com.rickmorty.Repository.UserRepository;
import com.rickmorty.exceptions.UserNotFoundException;
import com.rickmorty.exceptions.ValidationErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void saveUser(UserDto userDto, BindingResult result) {
        validateFields(userDto, result);

        UserModel userModel = new UserModel();
        userModel.setName(userDto.name());
        userModel.setSurname(userDto.surname());
        userModel.setEmail(userDto.email());
        userModel.setPassword(userDto.password());
        userModel.setDate_register(LocalDateTime.now());
        userRepository.save(userModel);
    }

    public void updateUser(Long id, UserDto userDto, BindingResult result) {
        validateFields(userDto, result);

        Optional<UserModel> optionalUser = userRepository.findByIdAndActive(id, 1);
        if (!optionalUser.isPresent()) throw new UserNotFoundException();

        UserModel user = optionalUser.get();
        user.setName(userDto.name());
        user.setSurname(userDto.surname());
        user.setEmail(userDto.email());
        user.setPassword(userDto.password());
        user.setDate_update(LocalDateTime.now());
        userRepository.save(user);
    }

    public void patchUser(Long id, UserPatchDto userPatchDto, BindingResult result) {
        validateFieldsPatch(userPatchDto, result);

        Optional<UserModel> optionalUser = userRepository.findByIdAndActive(id, 1);
        if (!optionalUser.isPresent()) throw new UserNotFoundException();

        UserModel user = optionalUser.get();
        if (userPatchDto.name() != null) {
            user.setName(userPatchDto.name());
        }
        if (userPatchDto.surname() != null) {
            user.setSurname(userPatchDto.surname());
        }
        if (userPatchDto.email() != null) {
            user.setEmail(userPatchDto.email());
        }
        if (userPatchDto.password() != null) {
            user.setPassword(userPatchDto.password());
        }
        user.setDate_update(LocalDateTime.now());
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        Optional<UserModel> optionalUser = userRepository.findByIdAndActive(id, 1);
        if (optionalUser.isPresent()) {
            UserModel userModel = optionalUser.get();
            userModel.setActive(0);
            userModel.setDeleted_at(LocalDateTime.now());
            userRepository.save(userModel);
        }
    }

    public void validateFields(UserDto userDto, BindingResult result) {
        Optional<UserModel> checkEmailExists = userRepository.findByEmail(userDto.email());
        if (checkEmailExists.isPresent()) throw new ValidationErrorException(List.of("Email já cadastrado"));

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            throw new ValidationErrorException(errors);
        }
    }

    public void validateFieldsPatch(UserPatchDto userPatchDto, BindingResult result) {
        if(userPatchDto.email() != null) {
            Optional<UserModel> checkEmailExists = userRepository.findByEmail(userPatchDto.email());
            if (checkEmailExists.isPresent()) throw new ValidationErrorException(List.of("Email já cadastrado"));
        }

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            throw new ValidationErrorException(errors);
        }
    }
}