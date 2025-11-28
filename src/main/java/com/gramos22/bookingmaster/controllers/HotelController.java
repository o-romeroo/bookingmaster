package com.gramos22.bookingmaster.controllers;

import com.gramos22.bookingmaster.dtos.HotelRequest;
import com.gramos22.bookingmaster.dtos.HotelResponse;
import com.gramos22.bookingmaster.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotels")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @PostMapping
    public void addHotel(@RequestBody HotelRequest hotelRequest) {
        hotelService.addHotel(hotelRequest);
    }

    @GetMapping("/{id}")
    public HotelResponse getHotelById(@PathVariable Long id) {
        return hotelService.getHotelById(id);
    }

    @GetMapping
    public List<HotelResponse> getAllHotels() {
        return hotelService.getAllHotels();
    }

    @DeleteMapping("/{id}")
    public void deleteHotelById(@PathVariable Long id) {
        hotelService.deleteHotelById(id);
    }
}