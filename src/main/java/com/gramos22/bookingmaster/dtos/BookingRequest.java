package com.gramos22.bookingmaster.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "Dados para criação de uma reserva")
public class BookingRequest {

    @Schema(description = "ID do usuário que está fazendo a reserva", example = "1", requiredMode = RequiredMode.REQUIRED)
    private Long userId;

    @Schema(description = "ID do hotel para a reserva", example = "1", requiredMode = RequiredMode.REQUIRED)
    private Long hotelId;

    @Schema(description = "Data de check-in", example = "2025-12-01", requiredMode = RequiredMode.REQUIRED)
    private LocalDate checkInDate;

    @Schema(description = "Data de check-out", example = "2025-12-05", requiredMode = RequiredMode.REQUIRED)
    private LocalDate checkOutDate;

    @Schema(description = "Lista de acompanhantes (documentos)", example = "[\"98765432100\", \"11122233344\"]")
    private List<String> companions;

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
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

    public List<String> getCompanions() {
        return companions;
    }

    public void setCompanions(List<String> companions) {
        this.companions = companions;
    }
}