package com.gramos22.bookingmaster.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Schema(description = "Dados para criação de um hotel")
public class HotelRequest {

    @Schema(description = "Nome do hotel", example = "Hotel Copacabana Palace", requiredMode = RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "Endereço completo do hotel", example = "Av. Atlântica, 1702 - Copacabana, Rio de Janeiro")
    private String address;

    @Schema(description = "Preço por noite por hóspede", example = "450.00", requiredMode = RequiredMode.REQUIRED)
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