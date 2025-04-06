package com.project.webserver.controller.external.controller;

import com.project.webserver.service.external.AirportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController

@RequestMapping("/airport")
public class AirportController {
    private final AirportService service = new AirportService();

    //------------------------------Flights------------------------------

    @GetMapping("/flights")
    public ResponseEntity<Object> getFlights() {
        return service.getFlights();
    }

    @GetMapping("/flights/{id}")
    public ResponseEntity<Object> getFlight(@PathVariable("id") String id) {
        return service.getFlight(id);
    }

    //------------------------------Cargo Bays------------------------------

    @GetMapping("/cargo")
    public ResponseEntity<Object> getAllCargoBays() {
        return service.getAllCargoBays();
    }
    
    @GetMapping("/cargo/{id}")
    public ResponseEntity<Object> getCargoBay(@PathVariable("id") String id) {
        return service.getCargoBay(id);
    }

    @GetMapping("/cargo/available")
    public ResponseEntity<Object> getAvailableCargoBays() {
        return service.getAvailableCargoBays();
    }

    @PostMapping("/cargo/{id}/{truckID}/reserve")
    public ResponseEntity<Object> reserveCargoBay(@PathVariable("id") String id,
    @PathVariable("truckID") String truckID) {
        return service.reserveCargoBay(id, truckID);
    }

    @PostMapping("/cargo/{id}/{truckID}/arrived")
    public ResponseEntity<Object> cargoTruckArrived(@PathVariable("id") String cargoID,
                                            @PathVariable("truckID") String truckID)
    {
        return service.truckArrivedCargo(cargoID, truckID);
    }

    @PostMapping("/cargo/{id}/{truckID}/release")
    public ResponseEntity<Object> releaseCargoBay(@PathVariable("id") String id,
                                          @PathVariable("truckID") String truckID) {
        return service.releaseCargoBay(id, truckID);
    }

    //------------------------------Parking Bays------------------------------

    @GetMapping("/parking")
    public ResponseEntity<Object> getAllParkingBays() {
        return service.getParkingBays();
    }

    @GetMapping("/parking/{id}")
    public ResponseEntity<Object> getParkingBay(@PathVariable("id") String id) {
        return service.getParkingBay(id);
    }

    @GetMapping("/parking/available")
    public ResponseEntity<Object> getAvailableParkingBays() {
        return service.getAvailableParkingBays();
    }

    @PostMapping("/parking/{id}/{truckID}/reserve")
    public ResponseEntity<Object> reserveParkingBay(@PathVariable("id") String id,
                                              @PathVariable("truckID") String truckID) {
        return service.reserveParkingBay(id, truckID);
    }

    @PostMapping("/parking/{id}/{truckID}/arrived")
    public ResponseEntity<Object> parkingTruckArrived(@PathVariable("id") String id,
                                              @PathVariable("truckID") String truckID) {
        return service.truckArrivedParking(id, truckID);
    }

    @PostMapping("/parking/{id}/{truckID}/release")
    public ResponseEntity<Object> releaseParkingBay(@PathVariable("id") String id,
                                            @PathVariable("truckID") String truckID) {
        return service.releaseParkingBay(id, truckID);
    }

    //NOTE: THIS METHOD IS FOR TESTING ONLY. DON'T CIRCUMVENT CONTROL MECHANISMS.
    public AirportService getAirportService() {
        return this.service;
    }
}
