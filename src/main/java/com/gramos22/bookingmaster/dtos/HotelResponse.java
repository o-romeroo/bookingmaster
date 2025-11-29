package com.gramos22.bookingmaster.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados de resposta de um hotel")
public class HotelResponse {

    @Schema(description = "ID único do hotel", example = "1")
    private Long id;

    @Schema(description = "Nome do hotel", example = "Hotel Copacabana Palace")
    private String name;

    @Schema(description = "Endereço completo do hotel", example = "Av. Atlântica, 1702 - Copacabana, Rio de Janeiro")
    private String address;

    @Schema(description = "Preço por noite por hóspede", example = "450.00")
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