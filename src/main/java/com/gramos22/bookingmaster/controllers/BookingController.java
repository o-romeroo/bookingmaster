package com.gramos22.bookingmaster.controllers;

import com.gramos22.bookingmaster.dtos.BookingRequest;
import com.gramos22.bookingmaster.dtos.BookingResponse;
import com.gramos22.bookingmaster.services.BookingService;
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
@RequestMapping("/bookings")
@Tag(name = "Bookings", description = "Gerenciamento de reservas")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar reserva", description = "Cria uma nova reserva de hotel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reserva criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Hotel ou usuário não encontrado")
    })
    public void createBooking(@RequestBody BookingRequest bookingRequest) {
        bookingService.createBooking(bookingRequest);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar reserva por ID", description = "Retorna os dados de uma reserva específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva encontrada"),
            @ApiResponse(responseCode = "404", description = "Reserva não encontrada")
    })
    public BookingResponse getBookingById(
            @Parameter(description = "ID da reserva", required = true)
            @PathVariable Long id) {
        return bookingService.getBookingById(id);
    }

    @GetMapping
    @Operation(summary = "Listar todas as reservas", description = "Retorna a lista de todas as reservas")
    @ApiResponse(responseCode = "200", description = "Lista de reservas retornada com sucesso")
    public List<BookingResponse> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Cancelar reserva", description = "Cancela uma reserva existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reserva cancelada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Reserva não encontrada")
    })
    public void cancelBooking(
            @Parameter(description = "ID da reserva", required = true)
            @PathVariable Long id) {
        bookingService.cancelBooking(id);
    }
}