package com.project.webserver.controller.external.controller;

import com.project.webserver.model.dali.UserLocation;
import com.project.webserver.service.external.DALIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.ws.rs.Consumes;

@RestController
@RequestMapping("/dali")
public class DALIController {
    private final DALIService service;

    public DALIController() {
        service = new DALIService();
    }

    @GetMapping("/controllers")
    public ResponseEntity<Object> getControllers() {
        return service.getControllers();
    }

    @GetMapping("/controller/{id}")
    public ResponseEntity<Object> getController(@PathVariable("id") String id) {
        //validate contorller exists
        return service.getController(id);
    }

    @Consumes("application/json")
    @PostMapping("/controller/{id}/{username}")
    public ResponseEntity<Object> updateController(@PathVariable("id") String id,
                                           @PathVariable("username") String username,
                                           @RequestBody UserLocation location) {
        return service.updateController(id, username, location);
    }

}
