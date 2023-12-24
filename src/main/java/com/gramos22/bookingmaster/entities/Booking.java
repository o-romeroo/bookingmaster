package com.gramos22.bookingmaster.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "bookings")
@AllArgsConstructor
public class Booking {

    @Id
    @Column(name = "booking_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "order_code", nullable = false)
    private String code;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "hotel_id", referencedColumnName = "hotel_id", nullable = false)
    private Hotel hotel;
    private List<String> companions;
    @Column(name = "order_date", nullable = false)
    private Date order_date;
    @Column(name = "check_in_date", nullable = false)
    private Date check_in;
    @Column(name = "check_out_date")
    private Date check_out;
    @Column(name = "price", nullable = false)
    private double price;
}
