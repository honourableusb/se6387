package com.project.webserver;

import com.project.webserver.controller.UserController;
import com.project.webserver.controller.external.controller.AirportController;
import com.project.webserver.model.User;
import com.project.webserver.model.airport.*;
import com.project.webserver.service.external.AirportService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.stream.Collectors;

import static com.project.webserver.TestUtilities.*;
import static com.project.webserver.service.Constants.*;

public class AirportServiceTests {

    //TODO adjust and finetune response evaluations

    private static AirportController controller;
    private static UserController userController;
    private static User user;
    private static User secondUser;
    private static User thirdUser;

    //------------------------------TEST EXECUTORS------------------------------

    @BeforeAll
    public static void setup() {
        //instantiate airport service and controller
        controller = new AirportController();
        userController = new UserController();
        user = generateUserProfile("test1", "testpass", "testVin",
                "abc1234", "test@email.com");
        secondUser = generateUserProfile("test2", "test2pass", "testvin2",
                "bcd2345", "test2@email.com");
        thirdUser = generateUserProfile("test3", "test3pass", "testvin3",
                "cde3456", "test2@email.com");
    }

    @AfterEach
    public void clearReservations() {
        Map<String, CargoBay> reservedCargo = (Map<String, CargoBay>) controller.getAllCargoBays().getBody();
        reservedCargo = reservedCargo.entrySet().stream().filter(a -> a.getValue().getState()
                .equals(CargoBayState.RESERVED)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        for (Map.Entry<String, CargoBay> entry : reservedCargo.entrySet()) {
            controller.releaseCargoBay(entry.getKey(), entry.getValue().getTruckID());
        }

        Map<String, ParkingBay> reservedParking = (Map<String, ParkingBay>) controller.getAllParkingBays().getBody();
        reservedParking = reservedParking.entrySet().stream().filter(a-> a.getValue()
                .equals(ParkingBayState.RESERVED)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        for (Map.Entry<String, ParkingBay> entry: reservedParking.entrySet()) {
            controller.releaseParkingBay(entry.getKey(), entry.getValue().getTruckID());
        }
    }

    //------------------------------FLIGHTS------------------------------

    @Test
    public void testAirportFlightDB() {
        ResponseEntity resp = controller.getFlights();
        checkOK(resp);
        Map<String, Flight> flights = (Map<String, Flight>) resp.getBody();
//        System.out.println(flights);
        Assertions.assertEquals(flights.size(), FLIGHT_STARTING_COUNT);
    }

    @Test
    public void testGetSingleFlight() {
        Flight flight = getSingleFlight();
        ResponseEntity resp = controller.getFlight(flight.getTailNumber());
        checkOK(resp);
//        System.out.println(resp.getBody());
        Assertions.assertEquals(flight, resp.getBody());
    }

    @Test
    public void testAirportUpdateFlight() {
        Flight flight = getSingleFlight();
        Flight old = Flight.of(flight);
//        System.out.println(flight);
        flight.setState(FlightState.DELAYED);
        AirportService service = controller.getAirportService();
        ResponseEntity resp = service.updateFlight(flight);
//        System.out.println(resp.getBody());
        checkOK(resp);
        flight = (Flight) controller.getFlight(flight.getTailNumber()).getBody();
//        System.out.println(flight);
        Assertions.assertNotEquals(old, flight);
    }

    private Flight getSingleFlight() {
        ResponseEntity resp = controller.getFlights();
        checkOK(resp);
        Map<String, Flight> flights = (Map<String, Flight>) resp.getBody();
        return flights.entrySet().iterator().next().getValue();
    }

    //------------------------------CARGO------------------------------

    @Test
    public void testAirportCargoDB() {
        ResponseEntity resp = controller.getAllCargoBays();
        checkOK(resp);
        Map<String, CargoBay> bays = (Map<String, CargoBay>) resp.getBody();
        Assertions.assertEquals(bays.size(), CARGO_BAY_COUNT);
    }

    @Test
    public void testGetAvailableCargoBays() {
        ResponseEntity resp = controller.getAvailableCargoBays();
        checkOK(resp);
        Map<String, CargoBay> bays = (Map<String, CargoBay>) resp.getBody();
        System.out.println(bays);
    }

    @Test
    public void testAirportCargoReserve() {
        getOrCreateUser(user, userController);
        CargoBay bay = getSingleCargoBay();
        ResponseEntity resp = controller.reserveCargoBay(bay.getId(), user.getUsername());
        checkOK(resp);
        bay = (CargoBay) controller.getCargoBay(bay.getId()).getBody();
        //assert that spot is reserved
        Assertions.assertEquals(CargoBayState.RESERVED, bay.getState());
    }

    @Test
    public void testAirportCargoReserveUnavailable() {
        getOrCreateUser(user, userController);
        CargoBay bay = getUnavailableCargoBay();
        ResponseEntity resp = controller.reserveCargoBay(bay.getId(), user.getUsername());
        checkNotOK(resp);
        String msg = (String) resp.getBody();
        System.out.println(msg);
        Assertions.assertTrue(msg.contains("is not available to reserve."));
    }

    @Test
    public void testAirportCargoReserveUserNotExists() {
        CargoBay bay = getSingleCargoBay();
        ResponseEntity resp = controller.reserveCargoBay(bay.getId(), thirdUser.getUsername());
        checkNotOK(resp);
        String msg = (String) resp.getBody();
        Assertions.assertTrue(msg.contains("not found"));
    }

    @Test
    public void testAirportCargoReserveBayNotExists() {
        getOrCreateUser(user, userController);
        ResponseEntity resp = controller.reserveParkingBay("thisWillNotWork", user.getUsername());
        checkNotOK(resp);
        String msg = (String) resp.getBody();
        Assertions.assertTrue(msg.contains("No bay with ID"));
    }

    @Test
    public void testAirportCargoArrive() {
        getOrCreateUser(user, userController);
        CargoBay bay = getSingleCargoBay();
        ResponseEntity resp = controller.reserveParkingBay(bay.getId(), user.getUsername());
        checkOK(resp);
        resp = controller.cargoTruckArrived(bay.getId(), user.getUsername());
        checkOK(resp);
    }

    @Test
    public void testAirportCargoArriveBayNotExists() {
        ResponseEntity resp = controller.cargoTruckArrived("blag", "blag");
        checkNotOK(resp);
        String msg = (String) resp.getBody();
        Assertions.assertTrue(msg.contains("No bay with ID"));
    }

    @Test
    public void testAirportCargoArriveNotReserved() {
        getOrCreateUser(user, userController);
        ResponseEntity resp = controller.cargoTruckArrived("1", user.getUsername());
        checkNotOK(resp);
        String msg = (String) resp.getBody();
        System.out.println(msg);
        Assertions.assertTrue(msg.contains("is not reserved by any truck"));
    }

    @Test
    public void testAirportCargoArriveWrongTruck() {
        getOrCreateUser(user, userController);
        getOrCreateUser(secondUser, userController);
        CargoBay bay = getSingleCargoBay();
        ResponseEntity resp = controller.reserveCargoBay(bay.getId(), user.getUsername());
        checkOK(resp);
        resp = controller.cargoTruckArrived(bay.getId(), secondUser.getUsername());
        checkNotOK(resp);
        String msg = (String) resp.getBody();
        Assertions.assertTrue(msg.contains("is not reserved by truck"));
    }

    @Test
    public void testAirportCargoArriveTruckNotExists() {
        getOrCreateUser(user, userController);
        CargoBay bay = getSingleCargoBay();
        ResponseEntity resp = controller.reserveCargoBay(bay.getId(), user.getUsername());
        checkOK(resp);
        resp = controller.cargoTruckArrived(bay.getId(), thirdUser.getUsername());
        checkNotOK(resp);
        String msg = (String) resp.getBody();
        Assertions.assertTrue(msg.contains("User %s not found".formatted(thirdUser.getUsername())));
    }

    @Test
    public void testReleaseCargo(){
        getOrCreateUser(user, userController);
        CargoBay bay = getSingleCargoBay();
        ResponseEntity resp = controller.reserveCargoBay(bay.getId(), user.getUsername());
        checkOK(resp);
        resp = controller.releaseCargoBay(bay.getId(), user.getUsername());
        checkOK(resp);
        String msg = (String) resp.getBody();
        Assertions.assertTrue(msg.equals("Cargo bay %s released".formatted(bay.getId())));
    }

    @Test
    public void testReleaseCargoBayNotExists(){
        getOrCreateUser(user, userController);
        ResponseEntity resp = controller.releaseCargoBay("thisWillFail", user.getUsername());
        checkNotOK(resp);
        //TODO message eval
    }

    @Test
    public void testReleaseCargoIncorrectTruck(){
        getOrCreateUser(user, userController);
        getOrCreateUser(secondUser, userController);
        CargoBay bay = getSingleCargoBay();
        ResponseEntity resp = controller.reserveCargoBay(bay.getId(), user.getUsername());
        checkOK(resp);
        resp = controller.releaseCargoBay(bay.getId(), secondUser.getUsername());
        checkNotOK(resp);
        //TODO string eval
    }

    @Test
    public void testGetBayForTruck() {

    }

    //------------------------------PARKING------------------------------

    @Test
    public void testAirportParkingDB() {

    }

    @Test
    public void testGetAvailableParkingBays() {

    }

    @Test
    public void testAirportParkingReserved() {

    }
    
    //------------------------------WORKERS------------------------------
    
    private CargoBay getSingleCargoBay() {
        Map<String, CargoBay> available = (Map<String, CargoBay>) controller.getAvailableCargoBays().getBody();
        return available.entrySet().iterator().next().getValue();
    }
    
    private ParkingBay getSingleParkingBay() {
        Map<String, ParkingBay> available = (Map<String, ParkingBay>) controller.getAvailableParkingBays().getBody();
        return available.entrySet().iterator().next().getValue();
    }

    private CargoBay getUnavailableCargoBay() {
        Map<String, CargoBay> available = (Map<String, CargoBay>) controller.getAllCargoBays().getBody();
        return available.entrySet().stream().filter(a-> !a.getValue().getState().equals(CargoBayState.AVAILABLE))
               .collect(Collectors.toMap(a-> a.getKey(), a->a.getValue()))
               .entrySet().iterator().next().getValue();
    }

    private ParkingBay getUnavailableParkingBay() {
        Map<String, ParkingBay> available = (Map<String, ParkingBay>) controller.getAllParkingBays().getBody();
        return available.entrySet().stream().filter(a-> !a.getValue().getState().equals(ParkingBayState.AVAILABLE))
                .collect(Collectors.toMap(a-> a.getKey(), a->a.getValue()))
                .entrySet().iterator().next().getValue();
    }
}
