package com.project.webserver.service;

import com.project.webserver.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UserService implements IUserService  {
    private final FirebaseService firebaseService = new FirebaseService();
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String addUserHandler(String username, String password, String vin
            ,String licensePlate, String email){
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setVin(vin);
        user.setEmail(email);
        user.setLicensePlate(licensePlate);
        return addUser(user);
    }

    private String addUser(User user){
//        System.out.println("Reached add User");
//        firebaseService=new FirebaseService();
//        firebaseService.addUserDB(user);
        return firebaseService.addUserDB(user);
    }

    private ResponseEntity<Object>authenticate(String username, String password){
//        System.out.println("Reached Login Service");
//        firebaseService=new FirebaseService();

        return firebaseService.authenticateUserDB(username,password);
    }

    public ResponseEntity<Object>authenticateHandler(String username, String password){
        return authenticate(username,password);
    }

    private String updatePassword(String email,String password){
//        firebaseService=new FirebaseService();
        return firebaseService.updatePassword(email,password);
    }
    public String updatePasswordHandler(String email, String password){
//            System.out.println("Reached Forget Password Service Handler");
            return updatePassword(email,password);
    }

//    public void setFirebaseService(FirebaseService firebaseService) { //This is for if we want to mock the firebase service.
//        // Not needed but could be nice to be aware of.
//        this.firebaseService = firebaseService;
//    }

    public String deleteUser(String username) {
        return this.firebaseService.deleteUser(username);
    }

    public ResponseEntity<Object>getUser(String username) {
        User user = this.firebaseService.getUser(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        return ResponseEntity.ok(user);
    }

    public ResponseEntity<Object> testUser() {
        logger.info("Generating test user");
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user.setVin("testvin");
        user.setEmail("email@test.com");
        user.setLicensePlate("ABC1234");
        try {
            addUser(user);
        } catch (Exception e) {
            logger.error("Error generating test user: ", e);
            ResponseEntity.status(HttpStatus.CONFLICT).body(e);
        }
        return ResponseEntity.ok(user);
    }
}
