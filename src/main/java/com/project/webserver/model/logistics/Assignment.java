package com.project.webserver.model.logistics;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.webserver.model.airport.Flight;
import com.project.webserver.model.airport.FlightState;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Assignment {
    private String assignmentNumber;
    private CargoType cargoType;
    private PriorityLevel priorityLevel;
    private AssignmentType assignmentType;  // "Pickup" or "Drop-off"
    private FlightState flightStatus;
    private String flightNumber;
    private AssignmentStatus assignmentStatus;
    public Assignment() {}
    // Getters and Setters
//    public String getAssignmentNumber() {
//        return assignmentNumber;
//    }
//
//    public void setAssignmentNumber(String assignmentNumber) {
//        this.assignmentNumber = assignmentNumber;
//    }
//
//    public String getCargoType() {
//        return cargoType;
//    }
//
//    public void setCargoType(String cargoType) {
//        this.cargoType = cargoType;
//    }
//
//    public String getPriorityLevel() {
//        return priorityLevel;
//    }
//
//    public void setPriorityLevel(String priorityLevel) {
//        this.priorityLevel = priorityLevel;
//    }
//
//    public String getAssignmentType() {
//        return assignmentType;
//    }
//
//    public void setAssignmentType(String assignmentType) {
//        this.assignmentType = assignmentType;
//    }
//
//    public String getAssignmentStatus() {
//        return assignmentStatus;
//    }
//    public void setAssignmentStatus(String assignmentStatus) {
////        System.out.println("setAssignmentStatus"+assignmentStatus);
//        this.assignmentStatus = assignmentStatus;
//    }
//    public String getFlightNumber() {
//        return flightNumber;
//    }
//    public void setFlightNumber(String flightNumber) {
//        this.flightNumber = flightNumber;
//    }
//    public String getFlightStatus() {
//        return flightStatus;
//    }
//    public void setFlightStatus(String flightStatus) {
//        this.flightStatus = flightStatus;
//    }

    @Override
    public String toString() {
        return "Assignment Number: %s\n".formatted(this.assignmentNumber) +
                "Cargo Type: %s\n".formatted(this.cargoType) +
                "Priority Level: %s\n".formatted(this.priorityLevel) +
                "Assignment Type: %s\n".formatted(this.assignmentType) +
                "Flight Number: %s\n".formatted(this.flightNumber) +  // Accessing flightNumber from Flight class
                "Flight Status: %s".formatted(this.flightStatus);         // Accessing flightStatus from Flight class
    }
}