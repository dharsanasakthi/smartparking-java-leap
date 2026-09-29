package com.example.parksmart.controller;

import com.example.parksmart.models.Booking;
import com.example.parksmart.service.BookingService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(
            @RequestParam Long slotId,
            @RequestParam String customerName,
            @RequestParam String vehicleNumber,
            @RequestParam LocalDateTime startTime,
            @RequestParam LocalDateTime endTime) {

        Booking booking =
                bookingService.createBooking(
                        slotId,
                        customerName,
                        vehicleNumber,
                        startTime,
                        endTime
                );

        return new ResponseEntity<>(
                booking,
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {

        return ResponseEntity.ok(
                bookingService.getAllBookings()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                bookingService.getBookingById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(
            @PathVariable Long id,
            @RequestBody Booking booking) {

        return ResponseEntity.ok(
                bookingService.updateBooking(id, booking)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBooking(
            @PathVariable Long id) {

        bookingService.deleteBooking(id);

        return ResponseEntity.ok(
                "Booking deleted successfully"
        );
    }
}