package com.project.webserver.model.logistics;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
    public String getAssignmentNumber() {
        return assignmentNumber;
    }

    public void setAssignmentNumber(String assignmentNumber) {
        this.assignmentNumber = assignmentNumber;
    }

    public CargoType getCargoType() {
        return cargoType;
    }

    public void setCargoType(CargoType cargoType) {
        this.cargoType = cargoType;
    }

    public PriorityLevel getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(PriorityLevel priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public AssignmentType getAssignmentType() {
        return assignmentType;
    }

    public void setAssignmentType(AssignmentType assignmentType) {
        this.assignmentType = assignmentType;
    }

    public AssignmentStatus getAssignmentStatus() {
        return assignmentStatus;
    }
    public void setAssignmentStatus(AssignmentStatus assignmentStatus) {
//        System.out.println("setAssignmentStatus"+assignmentStatus);
        this.assignmentStatus = assignmentStatus;
    }
    public String getFlightNumber() {
        return flightNumber;
    }
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }
    public FlightState getFlightStatus() {
        return flightStatus;
    }
    public void setFlightStatus(FlightState flightStatus) {
        this.flightStatus = flightStatus;
    }

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