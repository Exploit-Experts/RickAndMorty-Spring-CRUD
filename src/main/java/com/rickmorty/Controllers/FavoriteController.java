package com.rickmorty.Controllers;

import com.rickmorty.DTO.FavoriteResponseDto;
import com.rickmorty.Services.FavoriteService;
import org.springframework.data.domain.Page;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.rickmorty.DTO.FavoriteDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;

@RestController
@RequestMapping("/api/v1/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @Operation(summary = "Create a new favorite",
            description = "Create a new favorite for a user",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Favorite created"),
                    @ApiResponse(responseCode = "400", description = "Invalid input",
                                 content = @Content(mediaType = "application/json",
                                                    examples = @ExampleObject(value = "{\"message\": \"Erro no corpo da requisição: o JSON está mal formatado ou contém valores inválidos\"}"))),
                    @ApiResponse(responseCode = "400", description = "Invalid apiId",
                            content = @Content(mediaType = "application/json",
                                    examples = @ExampleObject(value = "{\"message\": \"Parâmetro apiId inválido. Deve ser um número positivo maior que zero\"}"))),
                    @ApiResponse(responseCode = "400", description = "Invalid userId",
                            content = @Content(mediaType = "application/json",
                                    examples = @ExampleObject(value = "{\"message\": \"Parâmetro userId inválido. Deve ser um número positivo maior que zero\"}"))),
                    @ApiResponse(responseCode = "404", description = "Item not found",
                            content = @Content(mediaType = "application/json",
                                    examples = @ExampleObject(value = "{\"message\": \"itemType(EPISODE/LOCATION/CHARACTER) não encontrado para o ID\"}"))),
                    @ApiResponse(responseCode = "409", description = "Conflict - Favorite already exists",
                                 content = @Content(mediaType = "application/json",
                                                    examples = @ExampleObject(value = "{\"message\": \"O favorito já está cadastrado\"}"))),
            })
    @PostMapping
    public ResponseEntity<Void> createFavorite(@RequestBody @Valid FavoriteDto favoriteDto, BindingResult result) {
        favoriteService.create(favoriteDto, result);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Get all favorites for a user",
            description = "Retrieve all favorites for a specific user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Favorites found"),
                    @ApiResponse(responseCode = "400", description = "Invalid parameter. Ex: send a letter in userId",
                            content = @Content(mediaType = "application/json",
                                    examples = @ExampleObject(value = "{\"message\": \"Parâmetro userId inválido.\"}"))),
                    @ApiResponse(responseCode = "404", description = "Favorites not found",
                                 content = @Content(mediaType = "application/json",
                                                    examples = @ExampleObject(value = "{\"message\": \"Não encontrado\"}"))),
                    @ApiResponse(responseCode = "404", description = "User not found",
                            content = @Content(mediaType = "application/json",
                                    examples = @ExampleObject(value = "{\"message\": \"Usuário não encontrado\"}"))),
            })
    @GetMapping("/{userId}")
    public ResponseEntity<Page<FavoriteResponseDto>> getAllFavorites(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {

        Page<FavoriteResponseDto> favorites = favoriteService.getAllFavorites(userId, page, size, sort);

        return ResponseEntity.ok(favorites);
    }

    @Operation(summary = "Remove a specific favorite for a user",
            description = "Remove a specific favorite for a user by user ID and favorite ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Favorite removed"),
                    @ApiResponse(responseCode = "400", description = "Invalid parameter. Ex: send a letter in userId",
                            content = @Content(mediaType = "application/json",
                                    examples = @ExampleObject(value = "{\"message\": \"Parâmetro userId inválido.\"}"))),
                    @ApiResponse(responseCode = "404", description = "Favorite not found", 
                                 content = @Content(mediaType = "application/json", 
                                                    examples = @ExampleObject(value = "{\"message\": \"Favorito não encontrado\"}"))),
                    @ApiResponse(responseCode = "404", description = "User not found",
                            content = @Content(mediaType = "application/json",
                                    examples = @ExampleObject(value = "{\"message\": \"Usuário não encontrado\"}"))),
            })
    @DeleteMapping("/{userId}/{favoriteId}")
    public ResponseEntity<String> removeFavorite(@PathVariable Long userId, @PathVariable Long favoriteId) {
        favoriteService.removeFavorite(userId, favoriteId);
        return new ResponseEntity<>("Favorito removido com sucesso.", HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Remove all favorites for a user",
            description = "Remove all favorites for a user by user ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "All favorites removed",
                    content = @Content(mediaType = "application/json", 
                                                    examples = @ExampleObject(value = "{\"message\": \"Todos os favoritos do usuário foram removidos com sucesso.\"}"))),
                    @ApiResponse(responseCode = "400", description = "Invalid parameter. Ex: send a letter in userId",
                            content = @Content(mediaType = "application/json",
                                    examples = @ExampleObject(value = "{\"message\": \"Parâmetro userId inválido.\"}"))),
                    @ApiResponse(responseCode = "404", description = "Favorites not found", 
                                 content = @Content(mediaType = "application/json", 
                                                    examples = @ExampleObject(value = "{\"message\": \"O usuário não tem favoritos cadastrados\"}"))),
                    @ApiResponse(responseCode = "404", description = "User not found",
                            content = @Content(mediaType = "application/json",
                                    examples = @ExampleObject(value = "{\"message\": \"Usuário não encontrado\"}"))),
            })
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> removeFavoritesByUserId(@PathVariable Long userId) {
        favoriteService.removeAllFavoritesByUserId(userId);
        return new ResponseEntity<>("Todos os favoritos do usuário foram removidos com sucesso.", HttpStatus.NO_CONTENT);
    }
}