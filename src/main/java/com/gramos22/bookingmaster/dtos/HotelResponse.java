package com.gramos22.bookingmaster.dtos;

public class HotelResponse {

    private Long id;
    private String name;
    private String address;
    private double nightPricePerGuest;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getNightPricePerGuest() {
        return nightPricePerGuest;
    }

    public void setNightPricePerGuest(double nightPricePerGuest) {
        this.nightPricePerGuest = nightPricePerGuest;
    }
}