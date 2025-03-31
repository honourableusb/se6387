package com.project.webserver.service;

import com.project.webserver.model.TrafficSignalDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DaliService {

    @Value("${dali.base-url}")
    private String daliBaseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public TrafficSignalDTO getSignalById(String signalId) {
        String url = daliBaseUrl + "/" + signalId;
        return restTemplate.getForObject(url, TrafficSignalDTO.class);
    }
}
