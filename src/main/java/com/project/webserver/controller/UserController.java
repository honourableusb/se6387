package com.project.webserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.project.webserver.service.UserService;

@RestController
public class UserController {
    private UserService userService;
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        // Add your authentication logic here (e.g., validate against a database)
        System.out.println("Reached Login");
        UserService userService = new UserService();
//        userService.setUsername(username);
//        userService.setPassword(password);
        return userService.authenticateHandler(username,password);
    }
    @PostMapping("/createUser")
    public String createUser(@RequestParam String username, @RequestParam String password,@RequestParam String vin,
                             @RequestParam String licensePlate,
                             @RequestParam String email) {
//        System.out.println("Reached CreateUser");
//        System.out.println(username);
//        System.out.println(password);
//        System.out.println(vin);
//        System.out.println(licensePlate);
//        System.out.println(email);
        userService=new UserService();
        return userService.addUserHandler(username, password, vin, licensePlate, email);
    }
    @PostMapping("/forgetPassword")
    public String forgetPassword(@RequestParam String email,@RequestParam  String password) {
        userService=new UserService();
        return userService.updatePasswordHandler(email,password);
    }
}
