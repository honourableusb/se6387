package com.project.webserver.model.dali;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class DALIAgent implements Runnable {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private String id;
    private LightColor color;
    private Coordinate location;
    private Map<String , Coordinate> vehicles; //TODO is this going to stay a String?
    private int timeRemaining;
    private Map<LightColor, Integer> timings; //in a real DALI agent these would not exist they would be dynamic
    //TODO map<direction, id> neighbors

    public DALIAgent() {
    }

    public String getId() {
        return id;
    }

    public LightColor getColor() {
        return color;
    }

    public Coordinate getLocation() {
        return location;
    }

    public Map<String, Coordinate> getVehicles() {
        return vehicles;
    }

    public int getTimeRemaining() {
        return timeRemaining;
    }

    public Map<LightColor, Integer> getTimings() {
        return timings;
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

    public void setVehicles(Map<String, Coordinate> vehicles) {
        this.vehicles = vehicles;
    }

    public void setTimeRemaining() {
        this.timeRemaining = this.timings.get(this.color);
    }

    public void setTimings(Map<LightColor, Integer> timings) {
        this.timings = timings;
    }

    public void addVehicle(String vehicle, Coordinate coordinate) {
        this.vehicles.put(vehicle, coordinate);
    }

    public void clearVehicles() {
        this.vehicles.clear();
    }

    @Override
    public String toString() {
        return  "DALI Agent ID: %s\n".formatted(this.id) +
                "Current Color: %s\n".formatted(this.color) +
                "Time Remaining: %d\n".formatted(this.timeRemaining) +
                "This agent has %d vehicles in queue.".formatted(this.vehicles.size());
    }
//TODO somewhere in all this i need to figure out how to mark vehicles as no longer mattering to this agent.
    //TODO is that going to be in this code or in the frontend or the service? who knows.
    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(1000);
                this.timeRemaining--;
                if (timeRemaining == 0) {
                    switch (this.color) {
                        case RED: {
                            color = LightColor.GREEN;
                            setTimeRemaining();
                        }
                        case YELLOW: {
                            color = LightColor.RED;
                            setTimeRemaining();
                        }
                        case GREEN: {
                            color = LightColor.YELLOW;
                            setTimeRemaining();
                        }
                    }
                    logger.info("Updated light color to %s for agent %s".formatted(color, id));
                }
            }
        } catch (Exception e) {
            logger.error("Exception occured: ", e);
        }
    }
}
