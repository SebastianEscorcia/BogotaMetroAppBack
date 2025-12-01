package com.sena.BogotaMetroApp.presentation.controller;

import com.sena.BogotaMetroApp.presentation.dto.CompraTicketRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.qr.QrResponseDTO;
import com.sena.BogotaMetroApp.services.ticket.IticketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final IticketService ticketService;


    @PostMapping("/comprar")
    public ResponseEntity<QrResponseDTO> comprarTicket(@RequestBody CompraTicketRequestDTO request) {
        QrResponseDTO response = ticketService.comprarViaje(
                request.getIdUsuario(),
                request.getIdViaje()
        );

        return ResponseEntity.ok(response);
    }
}
