package com.project.webserver.controller.external.controller;

import com.project.webserver.service.external.AirportService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController

//@RequestMapping("/airport")
public class AirportController {
    @Autowired
    private AirportService service;

    //------------------------------Flights------------------------------

    @GetMapping("/flights")
    public ResponseEntity getFlights() {
        return service.getFlights();
    }

    @GetMapping("flights/{flightNumber}")
    public ResponseEntity getFlight(@PathVariable String flightNumber) {
        return service.getFlight(flightNumber);
    }

    //------------------------------Cargo Bays------------------------------

    @GetMapping("/cargo")
    public ResponseEntity getAllCargoBays() {
        return service.getAllCargoBays();
    }

    @GetMapping("/cargo/{id}")
    public ResponseEntity getCargoBay(@PathParam("id") String id) {
        return service.getCargoBay(id);
    }

    @GetMapping("/cargo/available")
    public ResponseEntity getAvailableCargoBays() {
        return service.getAvailableCargoBays();
    }

    @PostMapping("/cargo/{id}/{truckID}/reserve")
    public ResponseEntity<String> reserveCargoBay(@PathParam("id") String id,
    @PathParam("truckID") String truckID) {
        return service.reserveCargoBay(id, truckID);
    }

    @PostMapping("/cargo/{id}/{truckID}/arrived")
    public ResponseEntity cargoTruckArrived(@PathParam("id") String cargoID,
                                            @PathParam("truckID") String truckID)
    {
        return service.truckArrivedCargo(cargoID, truckID);
    }

    @PostMapping("/cargo/{id}/{truckID}/release")
    public ResponseEntity releaseCargoBay(@PathParam("id") String id,
                                          @PathParam("truckID") String truckID) {
        return service.releaseCargoBay(id, truckID);
    }

    //------------------------------Parking Bays------------------------------

    @GetMapping("/parking")
    public ResponseEntity getAllParkingBays() {
        return service.getParkingBays();
    }

    @GetMapping("/parking/{id}")
    public ResponseEntity getParkingBay(@PathParam("id") String id) {
        return service.getParkingBay(id);
    }

    @GetMapping("/parking/available")
    public ResponseEntity getAvailableParkingBays() {
        return service.getAvailableParkingBays();
    }

    @PostMapping("/parking/{id}/{truckID}/reserve")
    public ResponseEntity reserveParkingBay(@PathParam("id") String id,
                                              @PathParam("truckID") String truckID) {
        return service.reserveParkingBay(id, truckID);
    }

    @PostMapping("/parking/{id}/{truckID}/arrived")
    public ResponseEntity parkingTruckArrived(@PathParam("id") String id,
                                              @PathParam("truckID") String truckID) {
        return service.truckArrivedParking(id, truckID);
    }

    @PostMapping("/parking/{id}/{truckID}/release")
    public ResponseEntity releaseParkingBay(@PathParam("id") String id,
                                            @PathParam("truckID") String truckID) {
        return service.releaseParkingBay(id, truckID);
    }

    //NOTE: THIS METHOD IS FOR TESTING ONLY. DON'T CIRCUMVENT CONTROL MECHANISMS.
    public AirportService getAirportService() {
        return this.service;
    }
}
