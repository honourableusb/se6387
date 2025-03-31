package com.project.webserver.controller;

import com.project.webserver.model.TrafficSignalDTO;
import com.project.webserver.service.DaliClientService;
import com.project.webserver.service.RouteService;
import com.project.webserver.model.RouteRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.webserver.model.TrafficSignalDTO;
import org.springframework.web.bind.annotation.*;
import java.io.InputStream;
import java.util.List;


import java.util.List;

@RestController
@RequestMapping("/api")  // ✅ Changed from /api/routes to /api
public class RouteController {

    private final RouteService routeService;

    @Autowired
    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @PostMapping("/routes/createRoute")
    public String createRoute(@RequestBody RouteRequest routeRequest) {
        System.out.println("Request Received");
        System.out.println("Origin Latitude: " + routeRequest.getOriginLatitude());
        System.out.println("Origin Longitude: " + routeRequest.getOriginLongitude());
        System.out.println("Destination Latitude: " + routeRequest.getDestinationLatitude());
        System.out.println("Destination Longitude: " + routeRequest.getDestinationLongitude());
        return routeService.createRoute(routeRequest);
    }

    @Autowired
    private DaliClientService daliClientService;

    @GetMapping("/signals/{signalId}")
    public TrafficSignalDTO getSignalById(@PathVariable String signalId) {
        return daliClientService.getSignalById(signalId);
    }

    private List<TrafficSignalDTO> loadSignalsFromJson() {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("signals.json");
            if (is == null) {
                System.err.println("❌ signals.json file NOT FOUND in resources folder");
                return List.of();
            }
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(is, new TypeReference<List<TrafficSignalDTO>>() {});
        } catch (Exception e) {
            System.err.println("❌ Error parsing signals.json:");
            e.printStackTrace();
            return List.of();
        }
    }

}
