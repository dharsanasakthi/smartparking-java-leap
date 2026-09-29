package com.example.parksmart.service;

import com.example.parksmart.models.Booking;
import com.example.parksmart.models.CheckInOut;
import com.example.parksmart.models.Slot;
import com.example.parksmart.repository.CheckInOutRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CheckInOutService {

    // Fixed penalty rate
    // ₹10 for every 30 minutes or part of 30 minutes
    private static final double PENALTY_PER_30_MINUTES = 10.0;

    private final CheckInOutRepository checkInOutRepository;
    private final BookingService bookingService;

    public CheckInOutService(
            CheckInOutRepository checkInOutRepository,
            BookingService bookingService) {

        this.checkInOutRepository = checkInOutRepository;
        this.bookingService = bookingService;
    }

    // ---------------------------------------------------------
    // CHECK-IN
    // ---------------------------------------------------------

    @Transactional
    public CheckInOut checkIn(Long bookingId) {

        // Find the booking
        Booking booking = bookingService.getBookingById(bookingId);

        // Check whether a check-in/check-out record already exists
        CheckInOut checkInOut =
                checkInOutRepository
                        .findByBookingId(bookingId)
                        .orElse(null);

        // If record exists and check-in has already happened
        if (checkInOut != null &&
                checkInOut.getCheckInTime() != null) {

            throw new RuntimeException(
                    "Vehicle has already checked in"
            );
        }

        // Create a new CheckInOut record if one doesn't exist
        if (checkInOut == null) {
            checkInOut = new CheckInOut();
            checkInOut.setBooking(booking);
        }

        // Record current check-in time
        checkInOut.setCheckInTime(LocalDateTime.now());

        // Update booking status
        booking.setStatus("CHECKED_IN");

        // Update slot status
        Slot slot = booking.getSlot();
        slot.setStatus("OCCUPIED");

        // Save check-in information
        return checkInOutRepository.save(checkInOut);
    }

    // ---------------------------------------------------------
    // CHECK-OUT
    // ---------------------------------------------------------

    @Transactional
    public CheckInOut checkOut(Long bookingId) {

        // Find the booking
        Booking booking = bookingService.getBookingById(bookingId);

        // Find the check-in record
        CheckInOut checkInOut =
                checkInOutRepository
                        .findByBookingId(bookingId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Vehicle has not checked in"
                                ));

        // Make sure the vehicle hasn't already checked out
        if (checkInOut.getCheckOutTime() != null) {

            throw new RuntimeException(
                    "Vehicle has already checked out"
            );
        }

        // Record the current checkout time
        LocalDateTime checkoutTime = LocalDateTime.now();

        checkInOut.setCheckOutTime(checkoutTime);

        // Default values
        long overstayMinutes = 0;
        double penalty = 0.0;

        // -----------------------------------------------------
        // OVERSTAY CALCULATION
        // -----------------------------------------------------

        if (checkoutTime.isAfter(booking.getEndTime())) {

            overstayMinutes =
                    Duration.between(
                            booking.getEndTime(),
                            checkoutTime
                    ).toMinutes();

            // Calculate number of 30-minute penalty units
            //
            // Example:
            // 10 minutes  -> 1 unit
            // 30 minutes  -> 1 unit
            // 31 minutes  -> 2 units
            // 60 minutes  -> 2 units
            // 61 minutes  -> 3 units

            long penaltyUnits =
                    (long) Math.ceil(
                            overstayMinutes / 30.0
                    );

            penalty =
                    penaltyUnits * PENALTY_PER_30_MINUTES;
        }

        // Store calculated overstay
        checkInOut.setOverstayMinutes(overstayMinutes);

        // Store calculated penalty
        checkInOut.setPenalty(penalty);

        // Update booking status
        booking.setStatus("COMPLETED");

        // Make parking slot available again
        Slot slot = booking.getSlot();
        slot.setStatus("AVAILABLE");

        // Save checkout information
        return checkInOutRepository.save(checkInOut);
    }

    // ---------------------------------------------------------
    // GET CHECK-IN / CHECK-OUT RECORD
    // ---------------------------------------------------------

    public CheckInOut getCheckInOutByBookingId(Long bookingId) {

        return checkInOutRepository
                .findByBookingId(bookingId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Check-in/check-out record not found"
                        ));
    }

    // ---------------------------------------------------------
    // GET ALL CHECK-IN / CHECK-OUT RECORDS
    // ---------------------------------------------------------

    public List<CheckInOut> getAllCheckInOutRecords() {

        return checkInOutRepository.findAll();
    }
}