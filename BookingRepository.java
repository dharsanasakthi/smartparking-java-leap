package com.example.parksmart.repository;

import com.example.parksmart.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findBySlotIdAndStartTimeLessThanAndEndTimeGreaterThan(
            Long slotId,
            LocalDateTime requestedEndTime,
            LocalDateTime requestedStartTime
    );
}