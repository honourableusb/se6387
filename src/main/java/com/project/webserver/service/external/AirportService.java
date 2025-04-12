package com.project.webserver.service.external;

import com.project.webserver.model.airport.*;
import com.project.webserver.service.FirebaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Random;

import java.util.*;
import java.util.stream.Collectors;

import static com.project.webserver.service.Constants.*;

@Service
public class AirportService {
    //TODO these three could have a partner on firebase for persistence
    static Map<String, Flight> flightDB;
    static Map<String, CargoBay> cargoBayDB;
    static Map<String, ParkingBay> parkingBayDB;
    FirebaseService firebaseService = new FirebaseService();

    public AirportService() {
        populateFlights();
        initializeCargoBays();
        initializeParkingBays();
        System.out.println("Flight DB contents in Airport Service Constructor: ");
        for (Map.Entry<String, Flight> entry : flightDB.entrySet()) {
            Flight flight = entry.getValue();
            System.out.println("Flight Number: " + flight.getTailNumber() + ", Status: " + flight.getState());
        }
    }

    //------------------------------service methods------------------------------

    //------------------------------flights------------------------------

    public ResponseEntity<Map<String, Flight>> getFlights() {
        return ResponseEntity.ok(flightDB); //returning all the items in the map is computationally expensive for us but given the amount of flights that we have to deal with right now will be fine
    }

    public ResponseEntity getFlight(String tail) {
        if (!flightExists(tail)) {
            return noFlightFound(tail);
        }
        return ResponseEntity.ok(flightDB.get(tail));
    }

    public Flight updateFlight(Flight flight) {
        //check flight exists
        if (!flightExists(flight.getTailNumber())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Flight " + flight.getFlightNumber() + " not found");
        }
        flightDB.put(flight.getTailNumber(), flight);
        return flightDB.get(flight.getTailNumber());
    }

    //------------------------------cargo bays------------------------------

    public ResponseEntity getAllCargoBays() {
        return ResponseEntity.ok(cargoBayDB);
    }

    public ResponseEntity getCargoBay(String id) {
        if (!bayExists(id, BayType.CARGO_BAY)) {
            //logger here
            return noBayFound(id);
        }
        return ResponseEntity.ok(cargoBayDB.get(id));
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
        if (!userExists(truckID)) {
            return userNotFound(truckID);
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
        if (!userExists(truckID)) {
            return userNotFound(truckID);
        }
        //if bay is reserved, but not by our truck
        if (!cargoBayReservedBy(cargoBayID, truckID)) {
            return wrongTruckForBay(cargoBayID, truckID);
        }
        cargoBayDB.get(cargoBayID).setState(CargoBayState.IN_USE);
        return truckArrived(cargoBayID, truckID);
    }

    public ResponseEntity releaseCargoBay(String id, String truckID) {
        //check bay exists
        if (!bayExists(id, BayType.CARGO_BAY)) {
            return noBayFound(id);
        }
        if (!userExists(truckID)) {
            return userNotFound(truckID);
        }
        if (!cargoBayReservedBy(id, truckID)) {
            return wrongTruckForBay(id, truckID);
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

    public ResponseEntity getParkingBay(String id) {
        if (!bayExists(id, BayType.PARKING_BAY)) {
            //logger here
            return noBayFound(id);
        }
        return ResponseEntity.ok(parkingBayDB.get(id));
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
        if (!userExists(truckID)) {
            return userNotFound(truckID);
        }
        //acquire mutex for bay -- nice to have
        //reserve bay
        parkingBayDB.get(bayID).setState(ParkingBayState.RESERVED);
        parkingBayDB.get(bayID).setTruckID(truckID);
        //logger
        return ResponseEntity.ok("Successfully reserved parking bay %s".formatted(bayID));
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
        if (!userExists(truckID)) {
            return userNotFound(truckID);
        }
        //if bay is reserved, but not by our truck
        if (!parkingBayReservedBy(parkingBayID, truckID)) {
            return wrongTruckForBay(parkingBayID, truckID);
        }
        parkingBayDB.get(parkingBayID).setState(ParkingBayState.IN_USE);
        return truckArrived(parkingBayID, truckID);
    }

    public ResponseEntity releaseParkingBay(String id, String truckID) {
        //check bay exists
        if (!bayExists(id, BayType.PARKING_BAY)) {
            return noBayFound(id);
        }
        if (!userExists(truckID)) {
            return userNotFound(truckID);
        }
        if (!parkingBayReservedBy(id, truckID)) {
            return wrongTruckForBay(id, truckID);
        }
        //remove truck id
        parkingBayDB.get(id).setTruckID("");
        //mark available
        parkingBayDB.get(id).setState(ParkingBayState.AVAILABLE);
        return ResponseEntity.ok("Parking bay %s released".formatted(id));
    }

    //truck arrived parking         //check if truck exists

    //------------------------------worker methods------------------------------

    public boolean flightExists(String wing) {
        wing = wing.trim().toUpperCase();
        System.out.println("Inside Flight exists:"+wing);

        for (Map.Entry<String, Flight> entry : flightDB.entrySet()) {
            String flightNumber = entry.getKey();
            Flight flight = entry.getValue();
            System.out.println("Flight Number: " + flightNumber + ", Flight Status: " + flight.getState() + ", Landing Time: " + flight.getLandingTime());
        }
//        System.out.println("Checking Wing:"+wing);
        return flightDB.containsKey(wing);
    }

    public ResponseEntity noFlightFound(String wing) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No flight %s found".formatted(wing));
    }

    public boolean bayExists(String id, BayType type) {
        return type == BayType.CARGO_BAY ? cargoBayDB.containsKey(id) : parkingBayDB.containsKey(id);
    }

    public boolean isCargoBayAvailable(String id) {
        return cargoBayDB.get(id).getState().equals(CargoBayState.AVAILABLE);
    }

    public boolean isParkingBayAvailable(String id) {
        return parkingBayDB.get(id).getState().equals(ParkingBayState.AVAILABLE);
    }

    public boolean userExists(String username) {
//        return this.firebaseService.getUser(username) != null;
        return true;
    }

    public boolean cargoBayReservedBy(String id, String truck) {
        return cargoBayDB.get(id).getTruckID().equals(truck);
    }

    public boolean parkingBayReservedBy(String id, String truck) {
        return parkingBayDB.get(id).getTruckID().equals(truck);
    }

    public ResponseEntity noBayFound(String id) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No bay with ID %s found.".formatted(id));
    }

    public ResponseEntity bayNotAvailable(String id) {
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                .body("Bay %s is not available to reserve. Please select another bay.".formatted(id));
    }

    public ResponseEntity userNotFound(String id) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("User %s not found".formatted(id));
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
        return ResponseEntity.ok("Marked truck %s as arrived to bay %s".formatted(truck, bay));
    }

    //------------------------------logistical methods------------------------------
    private void populateFlights() {
        flightDB = new HashMap<>();
        for (int i = 0; i < FLIGHT_STARTING_COUNT; i++) {
            Flight flight = generateFlight();
            flightDB.put(flight.getTailNumber(), flight);
        }
        System.out.println("Flight Populated:");
        for (Map.Entry<String, Flight> entry : flightDB.entrySet()) {
            System.out.println("TailNumber: " + entry.getKey() + ", Flight Details: " + entry.getValue());
        }
    }

    private Flight generateFlight() {
        Flight flight = new Flight();
        //generate landing time --> within the next 6 hours
        flight.setLandingTime(generateLandingTime());
        flight.setTailNumber(generateTailNumber());
        flight.setState(randomState());
        flight.setTerminal(generateTerminal());
        flight.setArrivalAirport(generateArrivalAirport());
        flight.setDestinationAirport(generateDestinationAirport());
        flight.setAirlineName(generateAirlineName());
        return flight;
    }
    private AirlineName generateAirlineName() {
        int val = new Random().nextInt(3);
        return switch (val) {
            case 0 -> AirlineName.AmericanAirlines;
            case 1 -> AirlineName.DeltaAirlines;
            case 2 -> AirlineName.SouthwestAirlines;
            default -> throw new IllegalStateException("Unexpected value: " + val);
        };
    }
    private ArrivalAirport generateArrivalAirport() {
        int val = new Random().nextInt(4);
        return switch (val) {
            case 0 -> ArrivalAirport.ATL;
            case 1 -> ArrivalAirport.JFK;
            case 2 -> ArrivalAirport.LAX;
            case 3 -> ArrivalAirport.ORD;
            default -> throw new IllegalStateException("Unexpected value: " + val);
        };
    }
    private DestinationAirport generateDestinationAirport() {
        return DestinationAirport.DFW;
    }
    private Date generateLandingTime() {
        long timestamp = System.currentTimeMillis();

        return new Date(timestamp);
    }

    private String generateTailNumber() {
        return "FDX%d".formatted(new Random().nextInt(9999));
    }

    private FlightState randomState() {
        int val = new Random().nextInt(1);
        return switch (val) {
//            case 0 -> FlightState.EARLY;
//            case 1 -> FlightState.DELAYED;
//            case 2 -> FlightState.ON_TIME;
            case 0 -> FlightState.LANDED;
            default -> throw new IllegalStateException("Unexpected value: " + val);
        };
    }

    public Terminal generateTerminal() {
        int val = new Random().nextInt(4);
        return switch (val) {
            case 0 -> Terminal.A;
            case 1 -> Terminal.B;
            case 2 -> Terminal.C;
            case 3 -> Terminal.D;
            default -> throw new IllegalStateException("Unexpected value: " + val);
        };
    }

    private void initializeCargoBays() { //I bet there's a way to break this into a more generic version
        cargoBayDB = new HashMap<>();
        Random rand = new Random();
        int unavailable = rand.nextInt(CARGO_BAY_COUNT);
        for (int i = 0; i < CARGO_BAY_COUNT; i++) {
            CargoBay bay = new CargoBay();
            bay.setId("CARGO#"+ i);
            if (i == unavailable) { //make one of them under maintenance
                bay.setState(CargoBayState.UNAVAILABLE);
            } else {
                bay.setState(CargoBayState.AVAILABLE);
            }
            cargoBayDB.put(bay.getId(), bay);
        }
    }

    private void initializeParkingBays() {
        parkingBayDB = new HashMap<>();
        Random rand = new Random();
        int unavailable = rand.nextInt(PARKING_BAY_COUNT);
        for (int i = 0; i < PARKING_BAY_COUNT; i++) {
            ParkingBay bay = new ParkingBay();
            bay.setId("PARK#"+ i);
            if (i == unavailable) {
                bay.setState(ParkingBayState.UNAVAILABLE);
            } else {
                bay.setState(ParkingBayState.AVAILABLE);
            }
            parkingBayDB.put(bay.getId(), bay);
        }
    }

}