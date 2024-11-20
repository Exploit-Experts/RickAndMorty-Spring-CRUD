package com.rickmorty.Controllers;

import com.rickmorty.DTO.ApiResponseDto;
import com.rickmorty.DTO.LocationDto;
import com.rickmorty.Services.LocationService;
import com.rickmorty.enums.SortLocation;

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

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LocationDto getLocationById(@PathVariable Long id) {
        return locationService.getLocationById(id);
    }
}
