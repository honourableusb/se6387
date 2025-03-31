package com.project.webserver.model;

public class TrafficSignalDTO {
    private String signalId;
    private String type;
    private int duration;
    private double recommendedSpeed;
    private double lat;
    private double lng;

    // Getters and Setters
    public String getSignalId() { return signalId; }
    public void setSignalId(String signalId) { this.signalId = signalId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public double getRecommendedSpeed() { return recommendedSpeed; }
    public void setRecommendedSpeed(double recommendedSpeed) { this.recommendedSpeed = recommendedSpeed; }

    public double getLat() { return lat; }
    public void setLat(double lat) { this.lat = lat; }

    public double getLng() { return lng; }
    public void setLng(double lng) { this.lng = lng; }
}
