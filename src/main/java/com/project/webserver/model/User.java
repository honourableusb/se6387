package com.project.webserver.model;
public class User {
    private String username="";
    private String password="";
    private String email="";
    private String vin="";
    private String licesePlate="";

//    Getter Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {

        return password;
    }
    public void setPassword(String password) {

        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getVin() {
        return vin;
    }
    public void setVin(String vin) {
        this.vin = vin;
    }
    public String getLicesePlate() {

        return licesePlate;
    }
    public void setLicesePlate(String licesePlate) {
        this.licesePlate = licesePlate;
    }

}
