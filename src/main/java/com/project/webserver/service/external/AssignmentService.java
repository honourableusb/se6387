package com.project.webserver.service.external;

import com.project.webserver.model.airport.Flight;
import com.project.webserver.model.airport.FlightState;
import com.project.webserver.model.logistics.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.project.webserver.service.Constants.ASSIGNMENT_STARTING_COUNT;
import static com.project.webserver.service.external.AirportService.flightDB;

@Service
public class AssignmentService {
    static Map<String, Assignment> assignmentDB = new HashMap<>();

    private final AirportService airportService; // Inject AirportService here

    @Autowired
    public AssignmentService(AirportService airportService) {
        this.airportService = airportService;
        populateAssignment();
    }
    public ResponseEntity<Map<String, Assignment>> getAssignments() {
        System.out.println("Assignments in DB: " + assignmentDB);
        return ResponseEntity.ok(assignmentDB);
    }
    public ResponseEntity getAssignment(String assignmentId) {
        if (!assignmentExists(assignmentId)) {
            return noAssignmentFound(assignmentId);
        }
        return ResponseEntity.ok(assignmentDB.get(assignmentId));
    }
    public boolean assignmentExists(String assignmentId) {
        return assignmentDB.containsKey(assignmentId);
    }
    public ResponseEntity noAssignmentFound(String assignmentId) {
        String errorMessage = "No Assignment %s found".formatted(assignmentId);
        System.out.println("Error response body: " + errorMessage);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Assignment %s found".formatted(assignmentId));
    }
    public Assignment updateAssignment(String assignmentNumber, Assignment assignment) {
        //check flight exists
        if(!airportService.flightNotExists(assignment.getFlightNumber())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Assignment " + assignment.getAssignmentNumber() + " not found");
        }
        assignmentDB.put(assignmentNumber, assignment);
        return assignmentDB.get(assignmentNumber);

    }
    private void populateAssignment() {
//        System.out.println("Populating Assignment");
        assignmentDB = new HashMap<>();
        int i = 0;
        for (Map.Entry<String, Flight> entry : flightDB.entrySet()) {
            if (i >= ASSIGNMENT_STARTING_COUNT) {
                break;  // Stop when you reach the desired number of assignments
            }
            Flight flight = entry.getValue();
            Assignment assignment = generateAssignment(flight);  // Pass the flight to the assignment
            assignmentDB.put(assignment.getAssignmentNumber(), assignment);
            i++;
        }

//        System.out.println("Assignment DB contents: ");
//        for (Map.Entry<String, Assignment> entry : assignmentDB.entrySet()) {
//            System.out.println(entry.getKey() + " = " + entry.getValue());
//        }
    }

    private Assignment generateAssignment(Flight flight) {
//        System.out.println("Generating Assignment");
        Assignment  assignment = new Assignment();
        assignment.setAssignmentNumber(generateAssignmentNumber());
        assignment.setAssignmentType(randomAssignmentType());
        assignment.setCargoType(randomCargoType());
        assignment.setPriorityLevel(randomPriorityLevel());
//        System.out.println("Generating AssignmentStatus");
        assignment.setAssignmentStatus(randomAssignmentStatus());
        assignment.setFlightNumber(flight.getTailNumber());
        assignment.setFlightStatus(flight.getState());
        return assignment;
    }
    private String generateAssignmentNumber() {
        return "%d".formatted(new Random().nextInt(9999));
    }
    private AssignmentStatus randomAssignmentStatus() {
//        System.out.println("Reached Assignment Status");
        int val = new Random().nextInt(3);
        return switch (val) {
            case 0 -> AssignmentStatus.COMPLETED;
            case 1 -> AssignmentStatus.IN_PROGRESS;
            case 2 -> AssignmentStatus.PENDING;
            default -> throw new IllegalStateException("Unexpected value: " + val);
        };
    }
    private PriorityLevel randomPriorityLevel() {
        int val = new Random().nextInt(2);
        return val == 0 ? PriorityLevel.HIGH : PriorityLevel.GENERAL;
    }
    private AssignmentType randomAssignmentType() {
        int val = new Random().nextInt(2);
        return val == 0 ? AssignmentType.PICKUP : AssignmentType.DROP;
    }
    private CargoType randomCargoType() {
        int val = new Random().nextInt(2);
        return val == 0 ? CargoType.CLOTHING : CargoType.ELECTRONIC;
    }

}
