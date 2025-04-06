package com.project.webserver;

import com.project.webserver.controller.external.client.GoogleMapsClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class GoogleMapsClientTests {
    static GoogleMapsClient client;

    @BeforeAll
    public static void setup() {
        client = new GoogleMapsClient();
    }

//    @Test
//    public void testClient() {
//        client.doPost("/directions/v2:computeRoutes", )
//    }
}
