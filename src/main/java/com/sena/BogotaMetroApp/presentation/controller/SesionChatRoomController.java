package com.sena.BogotaMetroApp.presentation.controller;

import com.sena.BogotaMetroApp.presentation.dto.sesionchat.AsignarSoporteDTO;
import com.sena.BogotaMetroApp.presentation.dto.sesionchat.SesionChatResponseDTO;
import com.sena.BogotaMetroApp.presentation.dto.sesionchat.SolicitudChatDTO;
import com.sena.BogotaMetroApp.services.SesionChatRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat-rooms")
@PreAuthorize("hasAnyRole('PASAJERO', 'SOPORTE')")
@RequiredArgsConstructor
public class SesionChatRoomController {
    private final SesionChatRoomService sesionChatRoomService;

    /**
     * Endpoint para que un pasajero solicite una sesión de chat con soporte.
     * @param dto Objeto que contiene el ID del pasajero que solicita el chat.
     * @return ResponseEntity con el ID de la sesión de chat creada.
     */

    @PostMapping("/solicitar")
    public ResponseEntity<Long> solicitarChat(@Valid @RequestBody SolicitudChatDTO dto) {
        Long idSesion = sesionChatRoomService.solicitarChatSoporte(dto.getIdUsuario());
        return ResponseEntity.ok(idSesion);
    }

    /**
     * Endpoint para que un soporte se una a sesión de chat.
     * @param idSesion ID de la sesión de chat a la que se asignará el soporte.
     * @param dto Objeto que contiene el ID del soporte a asignar.
     * @return ResponseEntity con un mensaje de confirmación.
     */
    @PutMapping("/{idSesion}/asignar")
    public ResponseEntity<String> asignarSoporte(
            @PathVariable Long idSesion,
            @Valid @RequestBody AsignarSoporteDTO dto) {

        sesionChatRoomService.asignarSoporteASesion(idSesion, dto.getIdUsuarioSoporte());
        return ResponseEntity.ok("Usuario soporte asignado correctamente a la sesión " + idSesion);
    }

    /**
     * Endpoint para cerrar una sesión de chat.
     * @param idSesion ID de la sesión de chat a cerrar.
     * @return ResponseEntity con un mensaje de confirmación.
     */

    @PutMapping("/{idSesion}/cerrar")
    public ResponseEntity<String> cerrarChat(@PathVariable Long idSesion) {
        sesionChatRoomService.cerrarSesion(idSesion);
        return ResponseEntity.ok("Sesión de chat finalizada");
    }

    /**
     * Obtener todas las sesiones pendientes (cola de espera para soporte)
     */
    @GetMapping("/pendientes")
    @PreAuthorize("hasRole('SOPORTE')")
    public ResponseEntity<List<?>> getSesionesPendientes() {
        return ResponseEntity.ok(sesionChatRoomService.obtenerSesionesPendientesDTO());
    }

    /**
     * Obtener las sesiones activas de un soporte específico
     */
    @GetMapping("/soporte/{idSoporte}/activas")
    @PreAuthorize("hasRole('SOPORTE')")
    public ResponseEntity<List<SesionChatResponseDTO>> getSesionesActivasSoporteDTO(@PathVariable Long idSoporte) {
        return ResponseEntity.ok(sesionChatRoomService.obtenerSesionesActivasPorSoporteDTO(idSoporte));
    }

    /**
     * Obtener detalles de una sesión específica
     */
    @GetMapping("/{idSesion}")
    public ResponseEntity<SesionChatResponseDTO> getSesionById(@PathVariable Long idSesion) {
        return ResponseEntity.ok(sesionChatRoomService.obtenerSesionPorId(idSesion));
    }

    /**
     * Obtener historial de mensajes de una sesión
     */
    @GetMapping("/{idSesion}/mensajes")
    public ResponseEntity<List<?>> getHistorialMensajes(@PathVariable Long idSesion) {
        return ResponseEntity.ok(sesionChatRoomService.obtenerMensajesPorSesion(idSesion));
    }

}
