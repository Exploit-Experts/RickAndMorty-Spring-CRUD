package com.rickmorty.Controllers;

import com.rickmorty.Models.LocationModel;
import com.rickmorty.Models.LocationResponse;
import com.rickmorty.Services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/api/locations")
public class LocationController {

    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public Mono<LocationResponse> getAllLocations() {
        return locationService.findAllLocations();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<LocationModel> findLocationById(@PathVariable String id){
        return locationService.findLocationById(id);
    }
}
