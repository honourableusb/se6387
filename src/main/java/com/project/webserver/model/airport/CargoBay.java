package com.project.webserver.model.airport;

import com.project.webserver.model.dali.Coordinate;

public class CargoBay {
    String id;
    CargoBayState state;
    Coordinate location;
    String truckID;

    public CargoBay() {
        truckID = "";
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setState(CargoBayState state) {
        this.state = state;
    }

    public void setTruckID(String id) {
        this.truckID = id;
    }

    public void setLocation(Coordinate location) {
        this.location = location;
    }

    //TODO maybe a clear truck id method?

    public String getId() {
        return this.id;
    }

    public CargoBayState getState() {
        return this.state;
    }

    public String getTruckID() {
        return this.truckID;
    }

    public Coordinate getLocation() {
        return location;
    }
}
