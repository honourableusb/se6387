package com.project.webserver.model.airport;

import java.util.Date;

public class Flight {
    String tailNumber;
    Date landingTime;
    FlightState state;
    //private int trucksNeeded //for if a flight needs more than one truck?
    //private CargoClassification highestClassification

    public Flight() {};

    public String getTailNumber() {
        return this.tailNumber;
    }

    public Date getLandingTime() {
        return this.landingTime;
    }

    public FlightState getState() {
        return this.state;
    }

    public void setTailNumber(String tailNumber) {
        this.tailNumber = tailNumber;
    }

    public void setLandingTime(Date landingTime) {
        this.landingTime = landingTime;
    }

    public void setState(FlightState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Flight Number: %s\n".formatted(this.tailNumber) +
                "Current Status: %s\n".formatted(this.state.name()) +
                "Landing Time: %s".formatted(this.landingTime);
        //TODO add trucks needed and highest classificatio nif implemented
    }



    public static Flight of(Flight flight) {
        Flight out = new Flight();
        out.setLandingTime(flight.getLandingTime());
        out.setState(flight.getState());
        out.setTailNumber(flight.getTailNumber());
        return out;
    }
}
