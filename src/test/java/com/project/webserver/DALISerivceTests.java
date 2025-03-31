package com.project.webserver;

import com.project.webserver.controller.external.controller.DALIController;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class DALISerivceTests {
    public static DALIController controller;

    @BeforeAll
    public static void setup() {
        controller = new DALIController();
        //other setup?
    }

    @Test
    public void testGetControllers() {

    }

    @Test
    public void testGetControllerState() {

    }

    @Test
    public void testGetControllerNotFound() {

    }

    @Test
    public void testUpdateController() {

    }

    @Test
    public void testUpdateControllerNotFound() {

    }

    @Test
    public void testUpdateControllerTruckNotExists() {

    }

//    @Test
//    public void testGetNextController?
//
}
