package com.gramos22.bookingmaster.utils;

import com.gramos22.bookingmaster.entities.Booking;
import com.gramos22.bookingmaster.repositories.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class ReportGenerator {

    private final BookingRepository bookingRepository;

    public Map<String, Long> generateHotelBookingReport() {
        List<Booking> bookings = bookingRepository.findAll();
        Map<String, Long> report = new HashMap<>();

        for (Booking booking : bookings) {
            String hotelName = booking.getHotel().getName();
            report.put(hotelName, report.getOrDefault(hotelName, 0L) + 1);
        }

        return report;
    }
}