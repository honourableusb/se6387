package com.project.webserver.service;

public class Constants {
    //------------------------------FLIGHTS------------------------------
    public final static int FLIGHT_STARTING_COUNT = 5;
    public final static int CARGO_BAY_COUNT = 12;
    public final static int PARKING_BAY_COUNT = 12;


    //------------------------------DALI------------------------------
    public final static int DALI_AGENT_COUNT = 13;
    public final static int DALI_REFRESH_RATE = 10; //frequency of how often dali should be polling
    public final static int DALI_DISTANCE_AWAY = 100; //distance away from agent before we send the next one, in meters
    public final static String DALI_COORDINATE_FILE = "src/main/resources/DALI_coordinates.json";
    public final static double DALI_CHANGE_MAX = .5; //how much we can adjust times by
}
