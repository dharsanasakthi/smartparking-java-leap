package com.example.parksmart.repository;

import com.example.parksmart.models.Slot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SlotRepository extends JpaRepository<Slot, Long> {

    List<Slot> findByStatus(String status);

    List<Slot> findByParkingLotId(Long parkingLotId);
}