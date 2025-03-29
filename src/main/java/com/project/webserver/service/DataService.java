package com.project.webserver.service;

import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.RestController;

//@SpringBootApplication
@RestController
public class DataService {
    //TODO logger

    public static void main(String[] args) {
        SpringApplication.run(DataService.class, args);
    }

}
