package com.project.webserver.model.dali;

import java.util.Map;

public class DALIAgentLocationData {
    String id;
    Coordinate coordinate;
    Map<Heading, String> neighbors;

    public DALIAgentLocationData() {
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNeighbors(Map<Heading, String> neighbors) {
        this.neighbors = neighbors;
    }

    public Map<Heading, String> getNeighbors() {
        return neighbors;
    }

    public String getId() {
        return id;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }
}
