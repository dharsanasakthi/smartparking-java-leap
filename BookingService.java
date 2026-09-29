package com.example.parksmart.service;

import com.example.parksmart.models.Booking;
import com.example.parksmart.models.Slot;
import com.example.parksmart.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final SlotService slotService;

    public BookingService(
            BookingRepository bookingRepository,
            SlotService slotService) {

        this.bookingRepository = bookingRepository;
        this.slotService = slotService;
    }

    public Booking createBooking(
            Long slotId,
            String customerName,
            String vehicleNumber,
            LocalDateTime startTime,
            LocalDateTime endTime) {

        if (customerName == null ||
                customerName.isBlank()) {

            throw new RuntimeException(
                    "Customer name is required");
        }

        if (vehicleNumber == null ||
                vehicleNumber.isBlank()) {

            throw new RuntimeException(
                    "Vehicle number is required");
        }

        if (startTime == null ||
                endTime == null) {

            throw new RuntimeException(
                    "Start time and end time are required");
        }

        if (!endTime.isAfter(startTime)) {

            throw new RuntimeException(
                    "End time must be after start time");
        }

        Slot slot = slotService.getSlotById(slotId);

        List<Booking> overlappingBookings =
                bookingRepository
                        .findBySlotIdAndStartTimeLessThanAndEndTimeGreaterThan(
                                slotId,
                                endTime,
                                startTime
                        );

        if (!overlappingBookings.isEmpty()) {

            throw new RuntimeException(
                    "Slot is already booked for the selected time window");
        }

        Booking booking = new Booking();

        booking.setSlot(slot);
        booking.setCustomerName(customerName);
        booking.setVehicleNumber(vehicleNumber);
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);
        booking.setStatus("BOOKED");

        return bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() {

        return bookingRepository.findAll();
    }

    public Booking getBookingById(Long id) {

        return bookingRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Booking not found with id: " + id));
    }

    public Booking updateBooking(
            Long id,
            Booking updatedBooking) {

        Booking existing = getBookingById(id);

        if (updatedBooking.getStartTime() != null &&
                updatedBooking.getEndTime() != null) {

            if (!updatedBooking.getEndTime()
                    .isAfter(updatedBooking.getStartTime())) {

                throw new RuntimeException(
                        "End time must be after start time");
            }

            Long slotId =
                    existing.getSlot().getId();

            List<Booking> overlappingBookings =
                    bookingRepository
                            .findBySlotIdAndStartTimeLessThanAndEndTimeGreaterThan(
                                    slotId,
                                    updatedBooking.getEndTime(),
                                    updatedBooking.getStartTime()
                            );

            boolean conflict =
                    overlappingBookings.stream()
                            .anyMatch(booking ->
                                    !booking.getId().equals(id));

            if (conflict) {

                throw new RuntimeException(
                        "Slot is already booked for the selected time window");
            }

            existing.setStartTime(
                    updatedBooking.getStartTime());

            existing.setEndTime(
                    updatedBooking.getEndTime());
        }

        if (updatedBooking.getCustomerName() != null &&
                !updatedBooking.getCustomerName().isBlank()) {

            existing.setCustomerName(
                    updatedBooking.getCustomerName());
        }

        if (updatedBooking.getVehicleNumber() != null &&
                !updatedBooking.getVehicleNumber().isBlank()) {

            existing.setVehicleNumber(
                    updatedBooking.getVehicleNumber());
        }

        return bookingRepository.save(existing);
    }

    public void deleteBooking(Long id) {

        Booking booking = getBookingById(id);

        bookingRepository.delete(booking);
    }
}