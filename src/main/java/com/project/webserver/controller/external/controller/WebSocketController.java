package com.project.webserver.controller.external.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.webserver.model.airport.Flight;
import com.project.webserver.model.logistics.Assignment;
import com.project.webserver.service.external.AirportService;
import com.project.webserver.service.external.AssignmentService;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Controller
//@Path("v1")
class MessageController {
//@Path("/updateAssignment")
@Autowired
private AssignmentService assignmentService;


    @MessageMapping("/application")  // Maps to /app/send in the WebSocket client
    @SendTo("/all/updateAssignment")  // Send the message to /topic/messages for clients to receive
    public Assignment updateAssignment(Assignment updateAssignment) throws JsonProcessingException {
        return assignmentService.updateAssignment(updateAssignment.getAssignmentNumber(),updateAssignment);
    }
    @Autowired
    private AirportService airportService;
    @MessageMapping("/updateFlight")  // Maps to /app/send in the WebSocket client
    @SendTo("/all/updateFlight")  // Send the message to /topic/messages for clients to receive
    public Flight updateFlight(Flight updateFlight) throws JsonProcessingException {
        System.out.println("Received update Flight:"+updateFlight);
        return airportService.updateFlight(updateFlight);
    }
    //    public String message(String message) {
//        System.out.println("Received Request:"+message);
//        return "Hi from server";
//    }

}
