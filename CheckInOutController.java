package com.example.parksmart.controller;

import com.example.parksmart.models.CheckInOut;
import com.example.parksmart.service.CheckInOutService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/check-in-out")
public class CheckInOutController {

    private final CheckInOutService checkInOutService;

    public CheckInOutController(CheckInOutService checkInOutService) {
        this.checkInOutService = checkInOutService;
    }

    @PostMapping("/check-in/{bookingId}")
    public ResponseEntity<CheckInOut> checkIn(
            @PathVariable Long bookingId) {

        return ResponseEntity.ok(
                checkInOutService.checkIn(bookingId)
        );
    }

    @PutMapping("/check-out/{bookingId}")
    public ResponseEntity<CheckInOut> checkOut(
            @PathVariable Long bookingId) {

        return ResponseEntity.ok(
                checkInOutService.checkOut(bookingId)
        );
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<CheckInOut> getCheckInOut(
            @PathVariable Long bookingId) {

        return ResponseEntity.ok(
                checkInOutService
                        .getCheckInOutByBookingId(bookingId)
        );
    }

    @GetMapping
    public ResponseEntity<List<CheckInOut>> getAllRecords() {

        return ResponseEntity.ok(
                checkInOutService.getAllCheckInOutRecords()
        );
    }
}