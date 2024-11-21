package com.rickmorty.Controllers;

import com.rickmorty.DTO.ApiResponseDto;
import com.rickmorty.DTO.LocationDto;
import com.rickmorty.Services.LocationService;
import com.rickmorty.enums.SortLocation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/locations")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @Operation(summary = "Get all locations",
            description = "Get all locations from the Rick and Morty series",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Locations found"),
                    @ApiResponse(responseCode = "404", description = "Locations not found", 
                                 content = @Content(mediaType = "application/json", 
                                                    examples = @ExampleObject(value = "{\"message\": \"Não encontrado\"}"))),
            })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponseDto<LocationDto>> getAllLocations(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "dimension", required = false) String dimension,
            @RequestParam(value = "sort", required = false) SortLocation sort) {
        ApiResponseDto<LocationDto> locations = locationService.findAllLocations(page, name, type, dimension, sort);
        return ResponseEntity.ok(locations);
    }

    @Operation(summary = "Get location by ID",
            description = "Get a specific location by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Location found"),
                    @ApiResponse(responseCode = "404", description = "Location not found", 
                                 content = @Content(mediaType = "application/json", 
                                                    examples = @ExampleObject(value = "{\"message\": \"Localizações não encontradas\"}"))),
            })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LocationDto getLocationById(@PathVariable Long id) {
        return locationService.getLocationById(id);
    }
}
