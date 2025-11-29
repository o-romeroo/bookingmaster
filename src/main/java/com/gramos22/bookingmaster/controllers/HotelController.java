package com.gramos22.bookingmaster.controllers;

import com.gramos22.bookingmaster.dtos.HotelRequest;
import com.gramos22.bookingmaster.dtos.HotelResponse;
import com.gramos22.bookingmaster.services.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotels")
@Tag(name = "Hotels", description = "Gerenciamento de hotéis")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar hotel", description = "Cadastra um novo hotel no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Hotel criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public void addHotel(@RequestBody HotelRequest hotelRequest) {
        hotelService.addHotel(hotelRequest);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar hotel por ID", description = "Retorna os dados de um hotel específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hotel encontrado"),
            @ApiResponse(responseCode = "404", description = "Hotel não encontrado")
    })
    public HotelResponse getHotelById(
            @Parameter(description = "ID do hotel", required = true)
            @PathVariable Long id) {
        return hotelService.getHotelById(id);
    }

    @GetMapping
    @Operation(summary = "Listar todos os hotéis", description = "Retorna a lista de todos os hotéis cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de hotéis retornada com sucesso")
    public List<HotelResponse> getAllHotels() {
        return hotelService.getAllHotels();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remover hotel", description = "Remove um hotel do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Hotel removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Hotel não encontrado")
    })
    public void deleteHotelById(
            @Parameter(description = "ID do hotel", required = true)
            @PathVariable Long id) {
        hotelService.deleteHotelById(id);
    }
}