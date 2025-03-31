package com.project.webserver.model.dali;


public class Coordinate {
    private float latitude;
    private float longitude;

    public Coordinate() { }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }
}
