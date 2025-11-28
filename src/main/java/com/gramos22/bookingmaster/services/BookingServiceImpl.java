package com.gramos22.bookingmaster.services;

import com.gramos22.bookingmaster.dtos.BookingRequest;
import com.gramos22.bookingmaster.dtos.BookingResponse;
import com.gramos22.bookingmaster.entities.Booking;
import com.gramos22.bookingmaster.entities.Hotel;
import com.gramos22.bookingmaster.entities.User;
import com.gramos22.bookingmaster.repositories.BookingRepository;
import com.gramos22.bookingmaster.repositories.HotelRepository;
import com.gramos22.bookingmaster.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;

    @Override
    public void createBooking(BookingRequest bookingRequest) {
        User user = userRepository.findById(bookingRequest.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        Hotel hotel = hotelRepository.findById(bookingRequest.getHotelId()).orElseThrow(() -> new RuntimeException("Hotel not found"));

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setHotel(hotel);
        booking.setCheckInDate(bookingRequest.getCheckInDate());
        booking.setCheckOutDate(bookingRequest.getCheckOutDate());
        bookingRepository.save(booking);
    }

    @Override
    public BookingResponse getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new RuntimeException("Booking not found"));
        return mapToResponse(booking);
    }

    @Override
    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public void cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new RuntimeException("Booking not found"));
        bookingRepository.delete(booking);
    }

    private BookingResponse mapToResponse(Booking booking) {
        BookingResponse response = new BookingResponse();
        response.setId(booking.getId());
        response.setUserName(booking.getUser().getFirst_name() + " " + booking.getUser().getLast_name());
        response.setHotelName(booking.getHotel().getName());
        response.setCheckInDate(booking.getCheckInDate());
        response.setCheckOutDate(booking.getCheckOutDate());
        return response;
    }
}