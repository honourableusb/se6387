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
}
