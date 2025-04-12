package com.project.webserver.model.airport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.util.RouteMatcher;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Flight {
    String flightNumber;
    Date landingTime;
    FlightState state;
    Terminal terminal;
    AirlineName airlineName;
    ArrivalAirport arrivalAirport;
    DestinationAirport destinationAirport;
    //private int trucksNeeded //for if a flight needs more than one truck?
    //private CargoClassification highestClassification

    public Flight() {};

    public String getTailNumber() {
        return this.flightNumber;
    }

    public Date getLandingTime() {
        return this.landingTime;
    }

    public FlightState getState() {
        return this.state;
    }

    public void setTailNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public void setLandingTime(Date landingTime) {
        this.landingTime = landingTime;
    }

    public void setState(FlightState state) {
        this.state = state;
    }

    public Terminal getTerminal() {
        return this.terminal;
    }
    public void setTerminal(Terminal Terminal) {
        this.terminal = Terminal;
    }
    @Override
    public String toString() {
        return "Flight Number: %s\n".formatted(this.flightNumber) +
                "Current Status: %s\n".formatted(this.state.name()) +
                "Landing Time: %s".formatted(this.landingTime)+
                "Arrival Airport: %s".formatted(this.arrivalAirport)+
                "Destination Airport: %s".formatted(this.destinationAirport)+
                "Airline Name: %s".formatted(this.airlineName);
        //TODO add trucks needed and highest classificatio nif implemented
    }



    public static Flight of(Flight flight) {
        Flight out = new Flight();
        out.setLandingTime(flight.getLandingTime());
        out.setState(flight.getState());
        out.setTailNumber(flight.getTailNumber());
        out.setAirlineName(flight.getAirlineName());
        out.setTerminal(flight.getTerminal());
        out.setDestinationAirport(flight.getDestinationAirport());
        out.setArrivalAirport(flight.getArrivalAirport());
        return out;
    }
}
