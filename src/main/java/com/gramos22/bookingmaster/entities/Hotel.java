package com.gramos22.bookingmaster.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "hotels")
@AllArgsConstructor
public class Hotel {
    @Id
    @Column(name = "hotel_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "hotel_name", nullable = false)
    private String name;
    @Column(name = "hotel_description")
    private String description;
    @Column(name = "hotel_address", nullable = false)
    private String address;
    @Column(name = "night_price_per_guest", nullable = false)
    private double night_price_per_guest;
}
