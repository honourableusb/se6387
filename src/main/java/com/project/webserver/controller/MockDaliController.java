package com.project.webserver.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.webserver.model.TrafficSignalDTO;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/mock-dali/signals") // This simulates the real DALI API base URL
public class MockDaliController {

    @GetMapping("/{signalId}")
    public TrafficSignalDTO getSignalById(@PathVariable String signalId) {
        List<TrafficSignalDTO> signals = loadSignalsFromJson();
        for (TrafficSignalDTO signal : signals) {
            if (signal.getSignalId().equalsIgnoreCase(signalId)) {
                return signal;
            }
        }
        return null; // or handle with proper 404
    }

    private List<TrafficSignalDTO> loadSignalsFromJson() {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("signals.json");
            if (is == null) {
                System.err.println("❌ signals.json file not found");
                return List.of();
            }
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(is, new TypeReference<List<TrafficSignalDTO>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
