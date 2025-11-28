package com.gramos22.bookingmaster.controllers;

import com.gramos22.bookingmaster.dtos.BookingRequest;
import com.gramos22.bookingmaster.dtos.BookingResponse;
import com.gramos22.bookingmaster.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public void createBooking(@RequestBody BookingRequest bookingRequest) {
        bookingService.createBooking(bookingRequest);
    }

    @GetMapping("/{id}")
    public BookingResponse getBookingById(@PathVariable Long id) {
        return bookingService.getBookingById(id);
    }

    @GetMapping
    public List<BookingResponse> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @DeleteMapping("/{id}")
    public void cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
    }
}