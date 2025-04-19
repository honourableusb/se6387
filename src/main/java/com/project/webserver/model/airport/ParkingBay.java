package com.project.webserver.model.airport;

import com.project.webserver.model.dali.Coordinate;

public class ParkingBay {
    String id;
    ParkingBayState state;
    Coordinate location;
    long truckArriveTime;
    String truckID;

    public ParkingBay() {
        truckID = "";
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setState(ParkingBayState state) {
        this.state = state;
    }

    public void setTruckArriveTime(long truckArriveTime) {
        this.truckArriveTime = truckArriveTime;
    }

    public void setTruckID(String truck) {
        this.truckID = truck;
    }

    public void setLocation(Coordinate location) {
        this.location = location;
    }

    public String getId() {
        return this.id;
    }

    public ParkingBayState getState() {
        return this.state;
    }

    public long getTruckArriveTime() {
        return this.truckArriveTime;
    }

    public String getTruckID() {
        return this.truckID;
    }

    public Coordinate getLocation() {
        return location;
    }
}
