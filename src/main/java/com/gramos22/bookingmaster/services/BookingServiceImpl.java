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

import java.sql.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;

    @Override
    public void createBooking(BookingRequest bookingRequest) {
        User user = userRepository.findById(bookingRequest.getUserId().intValue()).orElseThrow(() -> new RuntimeException("User not found"));
        Hotel hotel = hotelRepository.findById(bookingRequest.getHotelId()).orElseThrow(() -> new RuntimeException("Hotel not found"));

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setHotel(hotel);
        
        // Campos LocalDate (novos)
        booking.setCheckInDate(bookingRequest.getCheckInDate());
        booking.setCheckOutDate(bookingRequest.getCheckOutDate());
        
        // Campos Date legados (obrigatórios no banco)
        booking.setCheck_in(Date.valueOf(bookingRequest.getCheckInDate()));
        booking.setCheck_out(Date.valueOf(bookingRequest.getCheckOutDate()));
        
        // Campos obrigatórios
        booking.setOrder_date(new java.util.Date());
        booking.setCode(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        booking.setPrice(calculatePrice(hotel, bookingRequest));
        
        bookingRepository.save(booking);
    }
    
    private double calculatePrice(Hotel hotel, BookingRequest request) {
        long nights = java.time.temporal.ChronoUnit.DAYS.between(
            request.getCheckInDate(), 
            request.getCheckOutDate()
        );
        int guests = 1 + (request.getCompanions() != null ? request.getCompanions().size() : 0);
        return hotel.getNight_price_per_guest() * nights * guests;
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
        response.setPrice(booking.getPrice());
        return response;
    }
}