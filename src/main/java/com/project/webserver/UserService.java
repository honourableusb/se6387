package com.project.webserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserService {

    public static void main(String[] args) {
        System.out.println("hi");
        SpringApplication.run(UserService.class, args);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody Map<String, String> input) {
        System.out.println(input.get("id"));
        System.out.println(input.get("password"));
        return ResponseEntity.ok("Yeah i guess");
    }

    @GetMapping("/info")
    public ResponseEntity<Object> getUserInfo(@RequestParam("id") String id) {
        //userdb.get(id)
        System.out.println("hello");
        System.out.println(id);
        Map<String, String> response = new HashMap<>();
        response.put("id", id);
        response.put("vin", getRandomVin());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/")
    public ResponseEntity<Object> createUser(@RequestBody Map<String, String> userInfo) {
        // null checks here: what are we looking for? what's required?

        //any additional validations? does this account exist? do we want them to hit an /update api if it does?

        //push to db

        //return response
        return ResponseEntity.ok("ugh");
    }

    private static String getRandomVin() {
        return "testVin";
    }


}
