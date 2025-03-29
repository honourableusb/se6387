package com.project.webserver.controller.external.controller;

import com.project.webserver.model.airport.Flight;
import com.project.webserver.service.external.AirportService;
import jakarta.websocket.server.PathParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController

@RequestMapping("/airport")
public class AirportController {
    private AirportService service = new AirportService();

    //------------------------------Flights------------------------------

    @GetMapping("/flights")
    public ResponseEntity getFlights() {
        return service.getFlights();
    }

    //------------------------------Cargo Bays------------------------------

    @GetMapping("/cargo")
    public ResponseEntity getAllCargoSpaces() {
        return service.getAllCargoBays();
    }

    @GetMapping("/cargo/available")
    public ResponseEntity getAvailableCargoSpaces() {
        return service.getAvailableCargoBays();
    }

    @PostMapping("/cargo/{id}/{truckID}/reserve")
    public ResponseEntity reserveCargoSpace(@PathParam("id") String id,
    @PathParam("truckID") String truckID) {
        return service.reserveCargoBay(id, truckID);
    }

    @PostMapping("/cargo/{id}/{truckID}/arrived")
    public ResponseEntity cargoTruckArrived(@PathParam("id") String cargoID,
                                            @PathParam("truckID") String truckID)
    {
        return service.truckArrivedCargo(cargoID, truckID);
    }

    @PostMapping("/cargo/{id}/release")
    public ResponseEntity releaseCargoSpace(@PathParam("id") String id) {
        return service.releaseCargoBay(id);
    }

    //------------------------------Parking Bays------------------------------

    @GetMapping("/parking")
    public ResponseEntity getAllParkingSpaces() {
        return service.getParkingBays();
    }

    @GetMapping("/parking/available")
    public ResponseEntity getAvailableParkingSpaces() {
        return service.getAvailableParkingBays();
    }

    @PostMapping("/parking/{id}/{truckID}/reserve")
    public ResponseEntity reserveParkingSpace(@PathParam("id") String id,
                                              @PathParam("truckID") String truckID) {
        return service.reserveParkingBay(id, truckID);
    }

    @PostMapping("/parking/{id}/{truckID}/arrived")
    public ResponseEntity parkingTruckArrived(@PathParam("id") String id,
                                              @PathParam("truckID") String truckID) {
        return service.truckArrivedParking(id, truckID);
    }

    @PostMapping("/parking/{id}/release")
    public ResponseEntity releaseParkingSpace(@PathParam("id") String id) {
        return service.releaseParkingBay(id);
    }
}
