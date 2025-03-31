package com.project.webserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class RestServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestServiceApplication.class, args);
    }

    // ✅ Register RestTemplate bean so it can be injected
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
