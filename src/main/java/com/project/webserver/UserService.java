package com.project.webserver;

import org.apache.coyote.Response;
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

        //Actual logic
        //form connection to db, query for user id

        //validations on user id returning data

        //return data

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

    @PutMapping("/update")
    public ResponseEntity<Object> updateUserInformation(@RequestBody Map<String, String> updatedInfo) {
        //logic
        //get existing user account, throw precondition failed if none
        //if account exists, validate that the immutable fields (vin and size class?) are not changed, if they are then throw error

        //update other values in db
        //return 200
        return ResponseEntity.ok("yeah i guess that's updated");
    }

    private static String getRandomVin() {
        return "testVin";
    }


}
