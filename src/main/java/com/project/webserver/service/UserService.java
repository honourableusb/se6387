package com.project.webserver.service;

import com.project.webserver.model.User;
public class UserService implements IUserService  {
    private User user;
    private FirebaseService firebaseService = new FirebaseService();
    @Override
    public String addUserHandler(String username, String password,String vin
            ,String licensePlate, String email){

        System.out.println("Reached add user handler");
        user=new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setVin(vin);
        user.setEmail(email);
        user.setLicesePlate(licensePlate);
        return addUser();
    }

    private String addUser(){
        System.out.println("Reached add User");
//        firebaseService=new FirebaseService();
//        firebaseService.addUserDB(user);
        return firebaseService.addUserDB(user);
    }

    private boolean authenticate(String username,String password){
        System.out.println("Reached Login Service");
//        firebaseService=new FirebaseService();

        return firebaseService.authenticateUserDB(username,password);
    }

    public String authenticateHandler(String username, String password){
        boolean auth=authenticate(username,password);
        return auth ?"Login Successful":"Login Failed";
    }

    private String updatePassword(String email,String password){
//        firebaseService=new FirebaseService();
        return firebaseService.updatePassword(email,password);
    }
    public String updatePasswordHandler(String email, String password){
            System.out.println("Reached Forget Password Service Handler");
            return updatePassword(email,password);
    }

    public void setFirebaseService(FirebaseService firebaseService) { //This is for if we want to mock the firebase service.
        // Not needed but could be nice to be aware of.
        this.firebaseService = firebaseService;
    }

    public String deleteUser(User user) {
        return this.firebaseService.deleteUser(user);
    }
}
