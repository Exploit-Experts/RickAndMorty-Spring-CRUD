package com.rickmorty.Controllers;

import com.rickmorty.DTO.ApiResponseDto;
import com.rickmorty.DTO.LocationDto;
import com.rickmorty.Models.LocationModel;
import com.rickmorty.Services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {

    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponseDto<LocationDto>> getAllLocations(
            @RequestParam(value = "page", required = false) Integer page) {
        ApiResponseDto<LocationDto> locations = locationService.findAllLocations(page);
        return ResponseEntity.ok(locations);
    }
}
