package com.rickmorty.Controllers;

import com.rickmorty.DTO.UserDto;
import com.rickmorty.DTO.UserPatchDto;
import com.rickmorty.Services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Create a new user",
            description = "Create a new user with the provided details",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User created"),
                    @ApiResponse(responseCode = "400", description = "Invalid input", 
                                 content = @Content(mediaType = "application/json", 
                                                    examples = @ExampleObject(value = "{\"errors\": \"[]\"}"))),
                    @ApiResponse(responseCode = "409", description = "Conflict - Email already exists", 
                                 content = @Content(mediaType = "application/json", 
                                                    examples = @ExampleObject(value = "{\"message\": \"Email já cadastrado\"}"))),
            })
    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody @Valid UserDto userDto, BindingResult result) {
        userService.saveUser(userDto, result);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing user",
            description = "Update an existing user with the provided details",
            responses = {
                    @ApiResponse(responseCode = "204", description = "User updated"),
                    @ApiResponse(responseCode = "404", description = "User not found", 
                                 content = @Content(mediaType = "application/json", 
                                                    examples = @ExampleObject(value = "{\"message\": \"Usuário não encontrado\"}"))),
                    @ApiResponse(responseCode = "409", description = "Conflict - Email already exists", 
                                 content = @Content(mediaType = "application/json", 
                                                    examples = @ExampleObject(value = "{\"message\": \"Email já cadastrado\"}"))),
            })
    @PutMapping("{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid UserDto userDto, BindingResult result) {
        userService.updateUser(id, userDto, result);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Patch an existing user",
            description = "Patch an existing user with the provided details",
            responses = {
                    @ApiResponse(responseCode = "204", description = "User patched"),
                    @ApiResponse(responseCode = "404", description = "User not found", 
                                 content = @Content(mediaType = "application/json", 
                                                    examples = @ExampleObject(value = "{\"message\": \"Usuário não encontrado\"}"))),
                    @ApiResponse(responseCode = "409", description = "Conflict - Email already exists", 
                                 content = @Content(mediaType = "application/json", 
                                                    examples = @ExampleObject(value = "{\"message\": \"Email já cadastrado\"}"))),
            })
    @PatchMapping("{id}")
    public ResponseEntity<Void> patch(@PathVariable Long id, @RequestBody @Valid UserPatchDto userPatchDto, BindingResult result) {
        userService.patchUser(id, userPatchDto, result);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Delete a user",
            description = "Delete a user by its ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "User deleted"),
                    @ApiResponse(responseCode = "404", description = "User not found", 
                                 content = @Content(mediaType = "application/json", 
                                                    examples = @ExampleObject(value = "{\"message\": \"Usuário não encontrado\"}"))),
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}