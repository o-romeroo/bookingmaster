package com.gramos22.bookingmaster.services;

import com.gramos22.bookingmaster.dtos.HotelRequest;
import com.gramos22.bookingmaster.dtos.HotelResponse;
import com.gramos22.bookingmaster.entities.Hotel;
import com.gramos22.bookingmaster.repositories.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;

    @Override
    public void addHotel(HotelRequest hotelRequest) {
        Hotel hotel = new Hotel();
        hotel.setName(hotelRequest.getName());
        hotel.setAddress(hotelRequest.getAddress());
        hotel.setNight_price_per_guest(hotelRequest.getNightPricePerGuest());
        hotelRepository.save(hotel);
    }

    @Override
    public HotelResponse getHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new RuntimeException("Hotel not found"));
        return mapToResponse(hotel);
    }

    @Override
    public List<HotelResponse> getAllHotels() {
        return hotelRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public void deleteHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new RuntimeException("Hotel not found"));
        hotelRepository.delete(hotel);
    }

    private HotelResponse mapToResponse(Hotel hotel) {
        HotelResponse response = new HotelResponse();
        response.setId(hotel.getId());
        response.setName(hotel.getName());
        response.setAddress(hotel.getAddress());
        response.setNightPricePerGuest(hotel.getNight_price_per_guest());
        return response;
    }
}