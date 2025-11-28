package com.gramos22.bookingmaster.services;

import com.gramos22.bookingmaster.dtos.HotelRequest;
import com.gramos22.bookingmaster.dtos.HotelResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HotelService {

    void addHotel(HotelRequest hotelRequest);

    HotelResponse getHotelById(Long id);

    List<HotelResponse> getAllHotels();

    void deleteHotelById(Long id);
}