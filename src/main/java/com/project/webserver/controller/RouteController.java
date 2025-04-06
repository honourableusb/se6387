package com.project.webserver.controller;

import com.project.webserver.service.RouteService;
import com.project.webserver.model.RouteRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    private final RouteService routeService;
    @Autowired
    public RouteController(RouteService routeService) {
        this.routeService = routeService;  // Ensure RouteService is injected correctly
    }
    @PostMapping("/createRoute")
    public String createRoute(@RequestBody RouteRequest routeRequest) {
        // Call the RouteService to create a route based on origin and destination
        System.out.println("Request Received");
        System.out.println("Origin Latitude: " + routeRequest.getOriginLatitude());
        System.out.println("Origin Longitude: " + routeRequest.getOriginLongitude());
        System.out.println("Destination Latitude: " + routeRequest.getDestinationLatitude());
        System.out.println("Destination Longitude: " + routeRequest.getDestinationLongitude());
        return routeService.createRoute(routeRequest);

    }
}