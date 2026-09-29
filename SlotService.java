package com.example.parksmart.service;

import com.example.parksmart.models.ParkingLot;
import com.example.parksmart.models.Slot;
import com.example.parksmart.repository.SlotRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SlotService {

    private final SlotRepository slotRepository;
    private final ParkingLotService parkingLotService;

    public SlotService(
            SlotRepository slotRepository,
            ParkingLotService parkingLotService) {

        this.slotRepository = slotRepository;
        this.parkingLotService = parkingLotService;
    }

    public Slot createSlot(Long parkingLotId, Slot slot) {

        ParkingLot parkingLot =
                parkingLotService.getParkingLotById(parkingLotId);

        slot.setParkingLot(parkingLot);

        if (slot.getStatus() == null || slot.getStatus().isBlank()) {
            slot.setStatus("AVAILABLE");
        }

        return slotRepository.save(slot);
    }

    public List<Slot> getAllSlots() {
        return slotRepository.findAll();
    }

    public Slot getSlotById(Long id) {

        return slotRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Slot not found with id: " + id));
    }

    public List<Slot> getAvailableSlots() {

        return slotRepository.findByStatus("AVAILABLE");
    }

    public List<Slot> getSlotsByParkingLot(Long parkingLotId) {

        return slotRepository.findByParkingLotId(parkingLotId);
    }

    public Slot updateSlot(Long id, Slot updatedSlot) {

        Slot existing = getSlotById(id);

        existing.setSlotNumber(updatedSlot.getSlotNumber());

        if (updatedSlot.getStatus() != null &&
                !updatedSlot.getStatus().isBlank()) {

            existing.setStatus(updatedSlot.getStatus());
        }

        return slotRepository.save(existing);
    }

    public void deleteSlot(Long id) {

        Slot slot = getSlotById(id);

        slotRepository.delete(slot);
    }
}