package com.project.webserver.model.airport;

public class ParkingBay {
    String id;
    ParkingBayState state;
    //Point location; //for navigation directly to the position
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
}
