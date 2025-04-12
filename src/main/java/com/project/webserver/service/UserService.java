package com.project.webserver.service;

import com.project.webserver.model.User;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UserService implements IUserService  {
    private User user;
    private FirebaseService firebaseService = new FirebaseService();
    //TODO logger
    //TODO return responses from here, we want a bit more precision than Response.ok at the controller level

    @Override
    public String addUserHandler(String username, String password,String vin
            ,String licensePlate, String email){

        System.out.println("Reached add user handler");
        user=new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setVin(vin);
        user.setEmail(email);
        user.setLicensePlate(licensePlate);
        return addUser();
    }

    private String addUser(){
        System.out.println("Reached add User");
//        firebaseService=new FirebaseService();
//        firebaseService.addUserDB(user);
        return firebaseService.addUserDB(user);
    }

    private ResponseEntity authenticate(String username, String password){
        System.out.println("Reached Login Service");
//        firebaseService=new FirebaseService();

        return firebaseService.authenticateUserDB(username,password);
    }

    public ResponseEntity authenticateHandler(String username, String password){
        return authenticate(username,password);
    }

    private String updatePassword(String email,String password){
//        firebaseService=new FirebaseService();
        return firebaseService.updatePassword(email,password);
    }
    public String updatePasswordHandler(String email, String password){
            System.out.println("Reached Forget Password Service Handler");
            return updatePassword(email,password);
    }

//    public void setFirebaseService(FirebaseService firebaseService) { //This is for if we want to mock the firebase service.
//        // Not needed but could be nice to be aware of.
//        this.firebaseService = firebaseService;
//    }

    public String deleteUser(String username) {
        return this.firebaseService.deleteUser(username);
    }

    public ResponseEntity getUser(String username) {
//        User user = this.firebaseService.getUser(username);
//        if (user == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//        }
        return ResponseEntity.ok(user);
    }
}
