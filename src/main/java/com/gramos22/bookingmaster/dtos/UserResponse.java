package com.gramos22.bookingmaster.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Dados de resposta de um usuário")
public class UserResponse {

    @JsonProperty("user_id")
    @Schema(description = "ID único do usuário", example = "1")
    private int id;

    @JsonProperty("email")
    @Schema(description = "Email do usuário", example = "joao.silva@email.com")
    private String email;

    @JsonProperty("first_name")
    @Schema(description = "Primeiro nome do usuário", example = "João")
    private String first_name;

    @JsonProperty("last_name")
    @Schema(description = "Sobrenome do usuário", example = "Silva")
    private String last_name;

    @JsonProperty("document")
    @Schema(description = "Documento de identificação (CPF)", example = "12345678901")
    private String document;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
}
