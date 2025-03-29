package com.project.webserver.controller;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.project.webserver.service.UserService;

@RestController
//@RequestMapping("/user")
public class UserController {
    private final UserService userService = new UserService();

    @Consumes("application/json")
    @Produces("application/json")
    @PostMapping("/login")
    public ResponseEntity login(@RequestParam String username, @RequestParam String password) {
        // Add your authentication logic here (e.g., validate against a database)
        System.out.println("Reached Login");
//        userService.setUsername(username);
//        userService.setPassword(password);
        return userService.authenticateHandler(username,password);
    }

    @Consumes("application/json")
    @Produces("application/json")
    @PostMapping("/createUser")
    public ResponseEntity createUser(@RequestParam String username, @RequestParam String password,@RequestParam String vin,
                             @RequestParam String licensePlate,
                             @RequestParam String email) {
        return ResponseEntity.ok(userService.addUserHandler(username, password, vin, licensePlate, email));
    }

    @Consumes("application/json")
    @Produces("application/json")
    @PostMapping("/forgetPassword")
    public ResponseEntity forgetPassword(@RequestParam String email,@RequestParam  String password) {
        return ResponseEntity.ok(userService.updatePasswordHandler(email,password));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity deleteUser(@PathParam("username") String username) {
        return ResponseEntity.ok(userService.deleteUser(username));
    }

    @GetMapping("/{username}")
    public ResponseEntity getUser(@PathParam("username") String username) {
        return userService.getUser(username);
    }

    @GetMapping("/healthcheck")
    public ResponseEntity healthCheck() {
        return ResponseEntity.ok(true);
    }
}
