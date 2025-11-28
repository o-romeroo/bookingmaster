package com.gramos22.bookingmaster.services;

import com.gramos22.bookingmaster.dtos.BookingRequest;
import com.gramos22.bookingmaster.dtos.BookingResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookingService {

    void createBooking(BookingRequest bookingRequest);

    BookingResponse getBookingById(Long id);

    List<BookingResponse> getAllBookings();

    void cancelBooking(Long id);
}