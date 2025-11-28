package com.gramos22.bookingmaster;

import com.gramos22.bookingmaster.dtos.HotelRequest;
import com.gramos22.bookingmaster.dtos.HotelResponse;
import com.gramos22.bookingmaster.entities.Hotel;
import com.gramos22.bookingmaster.repositories.HotelRepository;
import com.gramos22.bookingmaster.services.HotelServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HotelServiceTests {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelServiceImpl hotelService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddHotel() {
        HotelRequest request = new HotelRequest();
        request.setName("Hotel Test");
        request.setAddress("123 Test St");
        request.setNightPricePerGuest(100.0);

        hotelService.addHotel(request);

        verify(hotelRepository, times(1)).save(any(Hotel.class));
    }

    @Test
    void testGetHotelById() {
        Hotel hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Hotel Test");
        hotel.setAddress("123 Test St");
        hotel.setNight_price_per_guest(100.0);

        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));

        HotelResponse response = hotelService.getHotelById(1L);

        assertNotNull(response);
        assertEquals("Hotel Test", response.getName());
        assertEquals("123 Test St", response.getAddress());
        assertEquals(100.0, response.getNightPricePerGuest());
    }

    @Test
    void testGetAllHotels() {
        Hotel hotel1 = new Hotel();
        hotel1.setId(1L);
        hotel1.setName("Hotel One");

        Hotel hotel2 = new Hotel();
        hotel2.setId(2L);
        hotel2.setName("Hotel Two");

        when(hotelRepository.findAll()).thenReturn(Arrays.asList(hotel1, hotel2));

        List<HotelResponse> hotels = hotelService.getAllHotels();

        assertEquals(2, hotels.size());
    }

    @Test
    void testDeleteHotelById() {
        Hotel hotel = new Hotel();
        hotel.setId(1L);

        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));

        hotelService.deleteHotelById(1L);

        verify(hotelRepository, times(1)).delete(hotel);
    }
}