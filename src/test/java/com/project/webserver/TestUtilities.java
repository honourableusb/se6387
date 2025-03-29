package com.project.webserver;

import org.junit.jupiter.api.Assertions;
import org.springframework.http.ResponseEntity;

public class TestUtilities {
    public static void checkOK(ResponseEntity resp) {
        Assertions.assertEquals(200, resp.getStatusCode().value());
    }

    public static void checkNotOK(ResponseEntity resp) {
        Assertions.assertNotEquals(200, resp.getStatusCode().value());
    }

    public static void checkEntity(ResponseEntity resp, Object object) {
        Assertions.assertEquals(resp.getBody(), object);
    }
}
