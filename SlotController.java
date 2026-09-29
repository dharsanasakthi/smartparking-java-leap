package com.example.parksmart.controller;

import com.example.parksmart.models.Slot;
import com.example.parksmart.service.SlotService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/slots")
public class SlotController {

    private final SlotService slotService;

    public SlotController(SlotService slotService) {
        this.slotService = slotService;
    }

    @PostMapping("/parking-lot/{parkingLotId}")
    public ResponseEntity<Slot> createSlot(
            @PathVariable Long parkingLotId,
            @RequestBody Slot slot) {

        return new ResponseEntity<>(
                slotService.createSlot(parkingLotId, slot),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<Slot>> getAllSlots() {

        return ResponseEntity.ok(
                slotService.getAllSlots()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Slot> getSlotById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                slotService.getSlotById(id)
        );
    }

    @GetMapping("/available")
    public ResponseEntity<List<Slot>> getAvailableSlots() {

        return ResponseEntity.ok(
                slotService.getAvailableSlots()
        );
    }

    @GetMapping("/parking-lot/{parkingLotId}")
    public ResponseEntity<List<Slot>> getSlotsByParkingLot(
            @PathVariable Long parkingLotId) {

        return ResponseEntity.ok(
                slotService.getSlotsByParkingLot(parkingLotId)
        );
    }

    @GetMapping("/occupancy")
    public ResponseEntity<List<Slot>> getOccupancy() {

        return ResponseEntity.ok(
                slotService.getAllSlots()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Slot> updateSlot(
            @PathVariable Long id,
            @RequestBody Slot slot) {

        return ResponseEntity.ok(
                slotService.updateSlot(id, slot)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSlot(
            @PathVariable Long id) {

        slotService.deleteSlot(id);

        return ResponseEntity.ok(
                "Slot deleted successfully"
        );
    }
}