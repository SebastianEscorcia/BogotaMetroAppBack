package com.sena.BogotaMetroApp.presentation.controller;

import com.sena.BogotaMetroApp.errors.ErrorCodeEnum;
import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.persistence.repository.UsuarioRepository;
import com.sena.BogotaMetroApp.presentation.dto.CompraTicketRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.qr.QrResponseDTO;
import com.sena.BogotaMetroApp.services.exception.usuario.UsuarioException;
import com.sena.BogotaMetroApp.services.ticket.IticketService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class TicketController {
    private final IticketService ticketService;
    private final UsuarioRepository usuarioRepository;

    @PostMapping("/comprar")
    public ResponseEntity<QrResponseDTO> comprarTicket(@RequestBody CompraTicketRequestDTO request, Authentication authentication) {
        Object principal = authentication.getPrincipal();
        Long idUsuarioAutenticado;

        if (principal instanceof Usuario) {
            idUsuarioAutenticado = ((Usuario) principal).getId();
        } else {

            String correo = authentication.getName();
            idUsuarioAutenticado = usuarioRepository.findByCorreo(correo)
                    .orElseThrow(() -> new UsuarioException(ErrorCodeEnum.USUARIO_NOT_FOUND))
                    .getId();
        }

        QrResponseDTO response = ticketService.comprarViaje(
                idUsuarioAutenticado,
                request.getIdViaje()
        );

        return ResponseEntity.ok(response);
    }
}
