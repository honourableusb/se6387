package com.project.webserver.model.dali;

public class UserLocation {
    Heading heading;
    Coordinate coordinate;

    public UserLocation() {
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public Heading getHeading() {
        return heading;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public void setHeading(Heading heading) {
        this.heading = heading;
    }
}
