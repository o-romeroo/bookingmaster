package com.gramos22.bookingmaster.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Dados de resposta de uma reserva")
public class BookingResponse {

    @Schema(description = "ID único da reserva", example = "1")
    private Long id;

    @Schema(description = "Nome do usuário que fez a reserva", example = "João Silva")
    private String userName;

    @Schema(description = "Nome do hotel reservado", example = "Hotel Copacabana Palace")
    private String hotelName;

    @Schema(description = "Data de check-in", example = "2025-12-01")
    private LocalDate checkInDate;

    @Schema(description = "Data de check-out", example = "2025-12-05")
    private LocalDate checkOutDate;

    @Schema(description = "Preço total da reserva", example = "1800.00")
    private double price;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}