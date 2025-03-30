package com.project.webserver;

import com.project.webserver.model.User;
import com.project.webserver.controller.UserController;
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

    public static User generateUserProfile(String userName, String password,
                                           String vin, String licensePlate, String email) {
        User resp = new User();
        resp.setUsername(userName);
        resp.setEmail(email);
        resp.setVin(vin);
        resp.setPassword(password);
        resp.setLicensePlate(licensePlate);
        return resp;
    }

    public static ResponseEntity createUser(User user, UserController controller) {
        return controller.createUser(user.getUsername(), user.getPassword(), user.getVin(), user.getLicensePlate(), user.getEmail());
    }

    public static ResponseEntity getOrCreateUser(User user, UserController controller) {
        ResponseEntity resp = controller.getUser(user.getUsername());
        if (resp.getStatusCode().value() == 200) {
            return resp;
        }
        else {
            return createUser(user, controller);
        }
    }
}
