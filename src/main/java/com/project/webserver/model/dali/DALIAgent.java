package com.project.webserver.model.dali;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.project.webserver.service.Constants.DALI_DISTANCE_AWAY;
import static com.project.webserver.service.Constants.DALI_REFRESH_RATE;

public class DALIAgent extends Thread {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private String id;
    private LightColor color;
    private Coordinate location;
    private Map<String , UserLocation> vehicles;
    private double timeRemaining;
    private Map<LightColor, Integer> timings; //in a real DALI agent these would not exist they would be dynamic
    private Map<Heading, String> neighbors;

    private final int frequency = DALI_REFRESH_RATE;

    public DALIAgent() {
    }

    public String getAgentID() {
        return id;
    }

    public LightColor getColor() {
        return color;
    }

    public Coordinate getLocation() {
        return location;
    }

    public Map<String, UserLocation> getVehicles() {
        return vehicles;
    }

    public double getTimeRemaining() {
        return timeRemaining;
    }

    public Map<LightColor, Integer> getTimings() {
        return timings;
    }

    public Map<Heading, String> getNeighbors() {
        return neighbors;
    }

    public String getNeighbor(Heading heading) {
        return neighbors.get(heading);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setColor(LightColor color) {
        this.color = color;
    }

    public void setLocation(Coordinate location) {
        this.location = location;
    }

    public void setVehicles(Map<String, UserLocation> vehicles) {
        this.vehicles = vehicles;
    }

    public void setTimeRemaining() {
        this.timeRemaining = this.timings.get(this.color);
    }

    public void setTimings(Map<LightColor, Integer> timings) {
        this.timings = timings;
    }

    public void setNeighbors(Map<Heading, String> neighbors) {
        this.neighbors = neighbors;
    }

    public void setNeighbor(Heading heading, String id) {
        this.neighbors.put(heading, id);
    }

    public void addVehicle(String vehicle, UserLocation location) {
        this.vehicles.put(vehicle, location);
    }

    public void clearVehicles() {
        this.vehicles.clear();
    }

    @Override
    public String toString() {
        return  "DALI Agent ID: %s\n".formatted(this.id) +
                "Current Color: %s\n".formatted(this.color) +
                "Time Remaining: %d\n".formatted(this.timeRemaining) +
                "This agent has %d vehicle(s) in queue.".formatted(this.vehicles.size());
    }

    public boolean hasUserPassed(UserLocation location, String username) {
        //return if distance is greater than old distance and greater than threshold
        double distance = calculateDistance(location);
        double previousDistance = calculateDistance(this.vehicles.get(username));
        double diff = distance - previousDistance; //positive means getting further away

        return distance >= DALI_DISTANCE_AWAY && diff > 0;
    }

    private double calculateDistance(UserLocation location) {
        Coordinate loc1 = this.location;
        Coordinate loc2 = location.getCoordinate();

        long r = 6371000; // m
        double p = Math.PI / 180;

        double a = 0.5 - Math.cos((loc2.getLatitude() - loc1.getLatitude()) * p) / 2
                        + Math.cos(loc1.getLatitude() * p) * Math.cos(loc2.getLatitude() * p) *
                        (1 - Math.cos((loc2.getLongitude() - loc1.getLongitude()) * p)) / 2;

        return 2 * r * Math.asin(Math.sqrt(a));
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(1000 / frequency);
                this.timeRemaining -= 1.0 / frequency;
                if (timeRemaining <= 0) {
                    switch (this.color) {
                        case RED: {
                            color = LightColor.GREEN;
                            setTimeRemaining();
                            break;
                        }
                        case YELLOW: {
                            color = LightColor.RED;
                            setTimeRemaining();
                            break;
                        }
                        case GREEN: {
                            color = LightColor.YELLOW;
                            setTimeRemaining();
                            clearVehicles();
                            break;
                        }
                    }
//                    logger.info("Updated light color to %s for agent %s".formatted(color, id));
                }
            }
        } catch (Exception e) {
            logger.error("Exception occured: ", e);
        }
    }
}
