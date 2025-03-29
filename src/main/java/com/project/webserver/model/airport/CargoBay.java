package com.project.webserver.model.airport;

public class CargoBay {
    String id;
    CargoBayState state;
    //Point location; //for the location to navigate to
    String truckID;

    public CargoBay() {

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
}
