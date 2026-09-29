package com.example.parksmart.service;

import com.example.parksmart.models.ParkingLot;
import com.example.parksmart.repository.ParkingLotRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingLotService {

    private final ParkingLotRepository parkingLotRepository;

    // Constructor
    public ParkingLotService(ParkingLotRepository parkingLotRepository) {
        this.parkingLotRepository = parkingLotRepository;
    }

    // ---------------------------------------------------------
    // CREATE PARKING LOT
    // ---------------------------------------------------------

    public ParkingLot createParkingLot(ParkingLot parkingLot) {

        if (parkingLot == null) {
            throw new RuntimeException("Parking lot data cannot be null");
        }

        if (parkingLot.getName() == null ||
                parkingLot.getName().trim().isEmpty()) {

            throw new RuntimeException(
                    "Parking lot name is required"
            );
        }

        if (parkingLot.getLocation() == null ||
                parkingLot.getLocation().trim().isEmpty()) {

            throw new RuntimeException(
                    "Parking lot location is required"
            );
        }

        return parkingLotRepository.save(parkingLot);
    }

    // ---------------------------------------------------------
    // GET ALL PARKING LOTS
    // ---------------------------------------------------------

    public List<ParkingLot> getAllParkingLots() {

        return parkingLotRepository.findAll();
    }

    // ---------------------------------------------------------
    // GET PARKING LOT BY ID
    // ---------------------------------------------------------

    public ParkingLot getParkingLotById(Long id) {

        if (id == null) {
            throw new RuntimeException(
                    "Parking lot ID cannot be null"
            );
        }

        return parkingLotRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Parking lot not found with ID: " + id
                        ));
    }

    // ---------------------------------------------------------
    // UPDATE PARKING LOT
    // ---------------------------------------------------------

    public ParkingLot updateParkingLot(
            Long id,
            ParkingLot updatedParkingLot) {

        ParkingLot existingParkingLot =
                getParkingLotById(id);

        if (updatedParkingLot == null) {
            throw new RuntimeException(
                    "Parking lot data cannot be null"
            );
        }

        if (updatedParkingLot.getName() == null ||
                updatedParkingLot.getName().trim().isEmpty()) {

            throw new RuntimeException(
                    "Parking lot name is required"
            );
        }

        if (updatedParkingLot.getLocation() == null ||
                updatedParkingLot.getLocation().trim().isEmpty()) {

            throw new RuntimeException(
                    "Parking lot location is required"
            );
        }

        existingParkingLot.setName(
                updatedParkingLot.getName()
        );

        existingParkingLot.setLocation(
                updatedParkingLot.getLocation()
        );

        return parkingLotRepository.save(existingParkingLot);
    }

    // ---------------------------------------------------------
    // DELETE PARKING LOT
    // ---------------------------------------------------------

    public void deleteParkingLot(Long id) {

        ParkingLot existingParkingLot =
                getParkingLotById(id);

        parkingLotRepository.delete(existingParkingLot);
    }
}