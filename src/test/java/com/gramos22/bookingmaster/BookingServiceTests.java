package com.gramos22.bookingmaster;

import com.gramos22.bookingmaster.dtos.BookingRequest;
import com.gramos22.bookingmaster.dtos.BookingResponse;
import com.gramos22.bookingmaster.entities.Booking;
import com.gramos22.bookingmaster.entities.Hotel;
import com.gramos22.bookingmaster.entities.User;
import com.gramos22.bookingmaster.repositories.BookingRepository;
import com.gramos22.bookingmaster.repositories.HotelRepository;
import com.gramos22.bookingmaster.repositories.UserRepository;
import com.gramos22.bookingmaster.services.BookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookingServiceTests {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBooking() {
        BookingRequest request = new BookingRequest();
        request.setUserId(1L);
        request.setHotelId(1L);
        request.setCheckInDate(LocalDate.now());
        request.setCheckOutDate(LocalDate.now().plusDays(2));

        User user = new User();
        user.setId(1);

        Hotel hotel = new Hotel();
        hotel.setId(1L);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));

        bookingService.createBooking(request);

        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void testGetBookingById() {
        Booking booking = new Booking();
        booking.setId(1L);
        User user = new User();
        user.setFirst_name("John");
        user.setLast_name("Doe");
        booking.setUser(user);
        Hotel hotel = new Hotel();
        hotel.setName("Hotel Test");
        booking.setHotel(hotel);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        BookingResponse response = bookingService.getBookingById(1L);

        assertNotNull(response);
        assertEquals("John Doe", response.getUserName());
        assertEquals("Hotel Test", response.getHotelName());
    }

    @Test
    void testGetAllBookings() {
        User user1 = new User();
        user1.setFirst_name("John");
        user1.setLast_name("Doe");
        
        Hotel hotel1 = new Hotel();
        hotel1.setName("Hotel One");
        
        Booking booking1 = new Booking();
        booking1.setId(1L);
        booking1.setUser(user1);
        booking1.setHotel(hotel1);

        User user2 = new User();
        user2.setFirst_name("Jane");
        user2.setLast_name("Doe");
        
        Hotel hotel2 = new Hotel();
        hotel2.setName("Hotel Two");
        
        Booking booking2 = new Booking();
        booking2.setId(2L);
        booking2.setUser(user2);
        booking2.setHotel(hotel2);

        when(bookingRepository.findAll()).thenReturn(Arrays.asList(booking1, booking2));

        List<BookingResponse> bookings = bookingService.getAllBookings();

        assertEquals(2, bookings.size());
    }

    @Test
    void testCancelBooking() {
        Booking booking = new Booking();
        booking.setId(1L);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        bookingService.cancelBooking(1L);

        verify(bookingRepository, times(1)).delete(booking);
    }
}