package com.gramos22.bookingmaster.dtos;

public class HotelRequest {

    private String name;
    private String address;
    private double nightPricePerGuest;

    // Getters and Setters
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