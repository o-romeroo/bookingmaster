package com.gramos22.bookingmaster.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Schema(description = "Dados para criação de um usuário")
public class UserRequest {

    @JsonProperty("email")
    @Schema(description = "Email do usuário", example = "joao.silva@email.com", requiredMode = RequiredMode.REQUIRED)
    private String email;

    @JsonProperty("first_name")
    @Schema(description = "Primeiro nome do usuário", example = "João", requiredMode = RequiredMode.REQUIRED)
    private String first_name;

    @JsonProperty("last_name")
    @Schema(description = "Sobrenome do usuário", example = "Silva", requiredMode = RequiredMode.REQUIRED)
    private String last_name;

    @JsonProperty("document")
    @Schema(description = "Documento de identificação (CPF)", example = "12345678901", requiredMode = RequiredMode.REQUIRED)
    private String document;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    // Alias methods for compatibility - hidden from JSON/Swagger
    @JsonIgnore
    public void setFirstName(String firstName) {
        this.first_name = firstName;
    }

    @JsonIgnore
    public void setLastName(String lastName) {
        this.last_name = lastName;
    }
}
