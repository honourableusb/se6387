package com.project.webserver;

public class Utilities {
    public static long fromNow(int months, int days, int hours, int minutes, int seconds) {
        long now = System.currentTimeMillis() / 1000;
        return (now + ((months * 2629743L) + (days * 86400L) + (hours * 3600L) + (minutes * 60L) + seconds)) * 1000;
    }

}
