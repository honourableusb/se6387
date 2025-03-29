package com.project.webserver.service.external;

import com.google.api.Http;
import com.project.webserver.model.airport.*;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Random;

import java.util.*;
import java.util.stream.Collectors;

public class AirportService {
    static Map<String, Flight> flightDB;
    static Map<String, CargoBay> cargoBayDB;
    static Map<String, ParkingBay> parkingBayDB;

    //constants
    final int FLIGHT_STARTING_COUNT = 5;
    final int CARGO_BAY_COUNT = 12;
    final int PARKING_BAY_COUNT = 12;

    public AirportService() {
        populateFlights();
        initializeCargoBays();
        initializeParkingBays();
    }

    //------------------------------service methods------------------------------

    //------------------------------flights------------------------------

    public ResponseEntity getFlights() {
        return ResponseEntity.ok(flightDB); //returning all the items in the map is computationally expensive for us but given the amount of flights that we have to deal with right now will be fine
    }

    //------------------------------cargo bays------------------------------

    public ResponseEntity getAllCargoBays() {
        return ResponseEntity.ok(cargoBayDB);
    }

    public ResponseEntity getAvailableCargoBays() {
        Map<String, CargoBay> available = cargoBayDB.entrySet().stream()
                .filter(bay-> bay.getValue().getState().equals(CargoBayState.AVAILABLE))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        if (available.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No cargo bays available");
        }
        else {
            return ResponseEntity.ok(available);
        }
    }

    public ResponseEntity reserveCargoBay(String bayID, String truckID) {
        //check if bay exists
        if (!bayExists(bayID, BayType.CARGO_BAY)) {
            //logger here
            return noBayFound(bayID);
        }
        //check if bay is reserved
        if (!isCargoBayAvailable(bayID)) {
            //logger here
            return bayNotAvailable(bayID);
        }
        //acquire mutex for bay -- nice to have
        //reserve bay
        cargoBayDB.get(bayID).setState(CargoBayState.RESERVED);
        cargoBayDB.get(bayID).setTruckID(truckID);
        //logger
        return ResponseEntity.ok("Successfully reserved cargo bay %s".formatted(bayID));
    }

    public ResponseEntity truckArrivedCargo(String cargoBayID, String truckID) {
        //check if bay exists
        if (!bayExists(cargoBayID, BayType.CARGO_BAY)) {
            //logger here
            return noBayFound(cargoBayID);
        }
        //check if bay is reserved
        if (isCargoBayAvailable(cargoBayID)) {
            //logger here
            return bayNotReserved(cargoBayID);
        }
        //check if truck exists

        //if bay is reserved, but not by our truck
        if (!cargoBayReservedBy(cargoBayID, truckID)) {
            return wrongTruckForBay(cargoBayID, truckID);
        }
        cargoBayDB.get(cargoBayID).setState(CargoBayState.IN_USE);
        return truckArrived(cargoBayID, truckID);
    }

    public ResponseEntity releaseCargoBay(String id) {
        //check bay exists
        if (!bayExists(id, BayType.CARGO_BAY)) {
            return noBayFound(id);
        }
        //remove truck id
        cargoBayDB.get(id).setTruckID("");
        //mark available
        cargoBayDB.get(id).setState(CargoBayState.AVAILABLE);
        return ResponseEntity.ok("Cargo bay %s released".formatted(id));
    }

    //------------------------------parking bays------------------------------

    public ResponseEntity getParkingBays() {
        return ResponseEntity.ok(parkingBayDB);
    }

    public ResponseEntity getAvailableParkingBays() {
        Map available = parkingBayDB.entrySet().stream().filter(a-> a.getValue().getState().equals(ParkingBayState.AVAILABLE))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        if (available.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No parking bays available");
        }
        return ResponseEntity.ok(available);
    }

    public ResponseEntity reserveParkingBay(String bayID, String truckID) {
        //check if bay exists
        if (!bayExists(bayID, BayType.PARKING_BAY)) {
            //logger here
            return noBayFound(bayID);
        }
        //check if bay is reserved
        if (!isParkingBayAvailable(bayID)) {
            //logger here
            return bayNotAvailable(bayID);
        }
        //acquire mutex for bay -- nice to have
        //reserve bay
        cargoBayDB.get(bayID).setState(CargoBayState.RESERVED);
        cargoBayDB.get(bayID).setTruckID(truckID);
        //logger
        return ResponseEntity.ok("Successfully reserved cargo bay %s".formatted(bayID));
    }

    public ResponseEntity truckArrivedParking(String parkingBayID, String truckID) {
        //check if bay exists
        if (!bayExists(parkingBayID, BayType.PARKING_BAY)) {
            //logger here
            return noBayFound(parkingBayID);
        }
        //check if bay is reserved
        if (isParkingBayAvailable(parkingBayID)) {
            //logger here
            return bayNotReserved(parkingBayID);
        }
        //check if truck exists

        //if bay is reserved, but not by our truck
        if (!cargoBayReservedBy(parkingBayID, truckID)) {
            return wrongTruckForBay(parkingBayID, truckID);
        }
        parkingBayDB.get(parkingBayID).setState(ParkingBayState.IN_USE);
        return truckArrived(parkingBayID, truckID);
    }

    public ResponseEntity releaseParkingBay(String id) {
        //check bay exists
        if (!bayExists(id, BayType.PARKING_BAY)) {
            return noBayFound(id);
        }
        //remove truck id
        parkingBayDB.get(id).setTruckID("");
        //mark available
        parkingBayDB.get(id).setState(ParkingBayState.AVAILABLE);
        return ResponseEntity.ok("Cargo bay %s released".formatted(id));
    }

    //truck arrived parking         //check if truck exists

    //------------------------------worker methods------------------------------
    public boolean bayExists(String id, BayType type) {
        return type == BayType.CARGO_BAY ? cargoBayDB.containsKey(id) : parkingBayDB.containsKey(id);
    }

    public boolean isCargoBayAvailable(String id) {
        return cargoBayDB.get(id).getState().equals(CargoBayState.AVAILABLE);
    }

    public boolean isParkingBayAvailable(String id) {
        return parkingBayDB.get(id).getState().equals(ParkingBayState.AVAILABLE)
    }

    public boolean cargoBayReservedBy(String id, String truck) {
        return cargoBayDB.get(id).getTruckID().equals(truck);
    }

    public ResponseEntity noBayFound(String id) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No bay with ID %s found.".formatted(id));
    }

    public ResponseEntity bayNotAvailable(String id) {
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                .body("Selected bay %s is not available to reserve. Please select another bay.".formatted(id));
    }

    public ResponseEntity bayNotReserved(String bay) {
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                .body("Bay %s is not reserved by any truck".formatted(bay));
    }

    public ResponseEntity wrongTruckForBay(String bay, String truck ) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Bay %s is not reserved by truck %s.".formatted(bay, truck));
    }

    public ResponseEntity truckArrived(String bay, String truck) {
        return ResponseEntity.ok("Marked truck %s as arrived to bay %s".formatted(bay, truck));
    }

    //------------------------------logistical methods------------------------------
    private void populateFlights() {
        flightDB = new HashMap<>();
        for (int i = 0; i < FLIGHT_STARTING_COUNT; i++) {
            Flight flight = generateFlight();
            flightDB.put(flight.getTailNumber(), flight);
        }
    }

    private Flight generateFlight() {
        Flight flight = new Flight();
        //generate landing time --> within the next 6 hours
        flight.setLandingTime(generateLandingTime());
        flight.setTailNumber(generateTailNumber());
        flight.setState(randomState());
        return flight;
    }

    private Date generateLandingTime() {
        long timestamp = System.currentTimeMillis();

        return new Date(timestamp);
    }

    private String generateTailNumber() {
        return "FDX%d".formatted(new Random().nextInt(9999));
    }

    private FlightState randomState() {
        int val = new Random().nextInt(3);
        return switch (val) {
            case 0 -> FlightState.EARLY;
            case 1 -> FlightState.DELAYED;
            case 2 -> FlightState.ON_TIME;
            default -> null;
        };
    }

    private void initializeCargoBays() { //I bet there's a way to break this into a more generic version
        cargoBayDB = new HashMap<>();
        Random rand = new Random();
        int unavailable = rand.nextInt(CARGO_BAY_COUNT);
        for (int i = 0; i < CARGO_BAY_COUNT; i++) {
            CargoBay bay = new CargoBay();
            bay.setId(String.valueOf(i));
            if (i == unavailable) { //make one of them under maintenance
                bay.setState(CargoBayState.UNAVAILABLE);
            } else {
                bay.setState(CargoBayState.AVAILABLE);
            }
        }
    }

    private void initializeParkingBays() {
        parkingBayDB = new HashMap<>();
        Random rand = new Random();
        int unavailable = rand.nextInt(PARKING_BAY_COUNT);
        for (int i = 0; i < PARKING_BAY_COUNT; i++) {
            ParkingBay bay = new ParkingBay();
            bay.setId(String.valueOf(i));
            if (i == unavailable) {
                bay.setState(ParkingBayState.UNAVAILABLE);
            } else {
                bay.setState(ParkingBayState.AVAILABLE);
            }
        }
    }

}