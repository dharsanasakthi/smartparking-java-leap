package com.example.parksmart.controller;

import com.example.parksmart.models.ParkingLot;
import com.example.parksmart.service.ParkingLotService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parking-lots")
@CrossOrigin(origins = "*")
public class ParkingLotController {

    private final ParkingLotService parkingLotService;

    public ParkingLotController(ParkingLotService parkingLotService) {
        this.parkingLotService = parkingLotService;
    }

    // ---------------------------------------------------------
    // CREATE PARKING LOT
    // POST /parking-lots
    // ---------------------------------------------------------

    @PostMapping
    public ResponseEntity<ParkingLot> createParkingLot(
            @RequestBody ParkingLot parkingLot) {

        ParkingLot createdParkingLot =
                parkingLotService.createParkingLot(parkingLot);

        return new ResponseEntity<>(
                createdParkingLot,
                HttpStatus.CREATED
        );
    }

    // ---------------------------------------------------------
    // GET ALL PARKING LOTS
    // GET /parking-lots
    // ---------------------------------------------------------

    @GetMapping
    public ResponseEntity<List<ParkingLot>> getAllParkingLots() {

        List<ParkingLot> parkingLots =
                parkingLotService.getAllParkingLots();

        return ResponseEntity.ok(parkingLots);
    }

    // ---------------------------------------------------------
    // GET PARKING LOT BY ID
    // GET /parking-lots/{id}
    // ---------------------------------------------------------

    @GetMapping("/{id}")
    public ResponseEntity<ParkingLot> getParkingLotById(
            @PathVariable Long id) {

        ParkingLot parkingLot =
                parkingLotService.getParkingLotById(id);

        return ResponseEntity.ok(parkingLot);
    }

    // ---------------------------------------------------------
    // UPDATE PARKING LOT
    // PUT /parking-lots/{id}
    // ---------------------------------------------------------

    @PutMapping("/{id}")
    public ResponseEntity<ParkingLot> updateParkingLot(
            @PathVariable Long id,
            @RequestBody ParkingLot parkingLot) {

        ParkingLot updatedParkingLot =
                parkingLotService.updateParkingLot(
                        id,
                        parkingLot
                );

        return ResponseEntity.ok(updatedParkingLot);
    }

    // ---------------------------------------------------------
    // DELETE PARKING LOT
    // DELETE /parking-lots/{id}
    // ---------------------------------------------------------

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteParkingLot(
            @PathVariable Long id) {

        parkingLotService.deleteParkingLot(id);

        return ResponseEntity.ok(
                "Parking lot deleted successfully"
        );
    }
}