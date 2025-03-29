package com.project.webserver.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

public interface IUserService {
    String addUserHandler(String username, String password,String vin
                        ,String licensePlate,
                        String email);
    ResponseEntity authenticateHandler(String username, String password);
    String updatePasswordHandler(String email, String password);

}
