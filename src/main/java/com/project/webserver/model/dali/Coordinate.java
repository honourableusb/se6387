package com.project.webserver.model.dali;


public class Coordinate implements Comparable<Coordinate>{
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

    @Override
    public int compareTo(Coordinate o) {
        return this.latitude == o.getLatitude() && this.longitude == o.getLongitude() ? 0 : 1;
    }
}
