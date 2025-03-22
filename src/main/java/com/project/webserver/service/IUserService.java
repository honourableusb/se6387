package com.project.webserver.service;

import org.springframework.web.bind.annotation.RequestParam;

public interface IUserService {
    String addUserHandler(String username, String password,String vin
                        ,String licensePlate,
                        String email);
    String authenticateHandler(String username, String password);

    String updatePasswordHandler(String email, String password);

}
