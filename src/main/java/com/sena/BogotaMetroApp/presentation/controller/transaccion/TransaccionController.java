package com.sena.BogotaMetroApp.presentation.controller.transaccion;

import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.persistence.repository.UsuarioRepository;
import com.sena.BogotaMetroApp.presentation.dto.transaccion.TransaccionRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.transaccion.TransaccionResponseDTO;
import com.sena.BogotaMetroApp.services.transaccion.ITransaccionService;
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
public class TransaccionController {

    private final ITransaccionService transaccionService;
    private final UsuarioRepository usuarioRepository;

    @PostMapping("/registrar")
    public ResponseEntity<TransaccionResponseDTO> registrarPago(@RequestBody TransaccionRequestDTO dto, Authentication authentication) {

        Object principal = authentication.getPrincipal();
        Usuario usuarioToken;

        if (principal instanceof Usuario) {
            usuarioToken = (Usuario) principal;
        } else {
            // Fallback por si acaso la configuración de seguridad cambia y devuelve solo el string
            String correo = authentication.getName();
            usuarioToken = usuarioRepository.findByCorreo(correo)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + correo));
        }

        dto.setIdUsuario(usuarioToken.getId());

        dto.setIdUsuario(usuarioToken.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transaccionService.registrarRecarga(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransaccionResponseDTO> obtenerPagoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(transaccionService.obtenerTransaccionPorId(id));
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<TransaccionResponseDTO>> obtenerPagosPorUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(transaccionService.obtenerTransaccionesPorUsuario(idUsuario));
    }

    @GetMapping("/pasarela/{idPasarela}")
    public ResponseEntity<List<TransaccionResponseDTO>> obtenerPagosPorPasarela(@PathVariable Long idPasarela) {
        return ResponseEntity.ok(transaccionService.obtenerTransaccionesPorPasarela(idPasarela));
    }

    @GetMapping("/referencia/{referencia}")
    public ResponseEntity<TransaccionResponseDTO> obtenerPagoPorReferencia(@PathVariable String referencia) {
        return ResponseEntity.ok(transaccionService.obtenerTransaccionPorReferencia(referencia));
    }

    @GetMapping("/fechas")
    public ResponseEntity<List<TransaccionResponseDTO>> obtenerPagosPorFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        return ResponseEntity.ok(transaccionService.obtenerTransaccionesPorFechas(fechaInicio, fechaFin));
    }

    @GetMapping("/usuario/{idUsuario}/fechas")
    public ResponseEntity<List<TransaccionResponseDTO>> obtenerPagosPorUsuarioYFechas(
            @PathVariable Long idUsuario,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        return ResponseEntity.ok(transaccionService.obtenerTransaccionesPorUsuarioYFechas(idUsuario, fechaInicio, fechaFin));
    }
}
