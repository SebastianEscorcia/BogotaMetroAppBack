package com.sena.BogotaMetroApp.presentation.controller.pago;

import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.persistence.repository.UsuarioRepository;
import com.sena.BogotaMetroApp.presentation.dto.pago.PagoRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.pago.PagoResponseDTO;
import com.sena.BogotaMetroApp.services.pago.IPagoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class PagoController {

    private final IPagoService pagoService;
    private final UsuarioRepository usuarioRepository;

    @PostMapping("/registrar")
    public ResponseEntity<PagoResponseDTO> registrarPago(@RequestBody PagoRequestDTO dto, Authentication authentication) {
        String correoAutenticado = authentication.getName();

        Usuario usuarioToken = usuarioRepository.findByCorreo(correoAutenticado)
                .orElseThrow(() -> new RuntimeException("Usuario del token no encontrado"));

        dto.setIdUsuario(usuarioToken.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pagoService.registrarPago(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoResponseDTO> obtenerPagoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.obtenerPagoPorId(id));
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<PagoResponseDTO>> obtenerPagosPorUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(pagoService.obtenerPagosPorUsuario(idUsuario));
    }

    @GetMapping("/pasarela/{idPasarela}")
    public ResponseEntity<List<PagoResponseDTO>> obtenerPagosPorPasarela(@PathVariable Long idPasarela) {
        return ResponseEntity.ok(pagoService.obtenerPagosPorPasarela(idPasarela));
    }

    @GetMapping("/referencia/{referencia}")
    public ResponseEntity<PagoResponseDTO> obtenerPagoPorReferencia(@PathVariable String referencia) {
        return ResponseEntity.ok(pagoService.obtenerPagoPorReferencia(referencia));
    }

    @GetMapping("/fechas")
    public ResponseEntity<List<PagoResponseDTO>> obtenerPagosPorFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        return ResponseEntity.ok(pagoService.obtenerPagosPorFechas(fechaInicio, fechaFin));
    }

    @GetMapping("/usuario/{idUsuario}/fechas")
    public ResponseEntity<List<PagoResponseDTO>> obtenerPagosPorUsuarioYFechas(
            @PathVariable Long idUsuario,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        return ResponseEntity.ok(pagoService.obtenerPagosPorUsuarioYFechas(idUsuario, fechaInicio, fechaFin));
    }
}
