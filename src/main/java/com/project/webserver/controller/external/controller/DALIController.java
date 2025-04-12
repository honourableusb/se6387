package com.project.webserver.controller.external.controller;

import com.project.webserver.model.User;
import com.project.webserver.model.dali.Coordinate;
import com.project.webserver.service.external.DALIService;
import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dali")
public class DALIController {
    private DALIService service;

    public DALIController() {
//        service = new DALIService();
    }

    @GetMapping("/controllers")
    public ResponseEntity getControllers() {
        return service.getControllers();
    }

    @GetMapping("/controller/{id}")
    public ResponseEntity getController(@PathParam("id") String id) {
        //validate contorller exists
        return service.getController(id);
    }

    @PostMapping("/controller/{id}")
    public ResponseEntity updateController(@PathParam("id") String id,
                                           @PathParam("username") String username,
                                           @RequestBody Coordinate coordinate) {
        //validate controller exists
        return service.updateController(id, username, coordinate);
        //validate user exists
    }

    /*
    @GetMapping("/controller/{id}/next")
    public ResponseEntity getNextController(@PathParam("id") String id,
                                            @RequestParam("heading") Heading heading) {

    }
     */
}
