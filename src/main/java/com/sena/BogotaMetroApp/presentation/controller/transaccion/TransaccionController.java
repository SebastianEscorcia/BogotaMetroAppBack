package com.sena.BogotaMetroApp.presentation.controller.transaccion;

import com.sena.BogotaMetroApp.errors.ErrorCodeEnum;

import com.sena.BogotaMetroApp.persistence.models.transaccion.Recarga;
import com.sena.BogotaMetroApp.presentation.dto.transaccion.PasarSaldoRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.transaccion.TransaccionRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.transaccion.TransaccionResponseDTO;
import com.sena.BogotaMetroApp.security.TransaccionSecurityService;
import com.sena.BogotaMetroApp.services.exception.usuario.UsuarioException;
import com.sena.BogotaMetroApp.services.transaccion.ITransaccionService;
import com.sena.BogotaMetroApp.utils.enums.MedioPagoEnum;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class TransaccionController {

    private final ITransaccionService transaccionService;
    private final TransaccionSecurityService transaccionSecurityService;

    @PreAuthorize("hasRole('PASAJERO')")
    @PostMapping("/registrar")
    public ResponseEntity<TransaccionResponseDTO> registrarPago(@RequestBody TransaccionRequestDTO dto) {

        dto.setIdUsuario(transaccionSecurityService.obtenerIdUsuarioAutenticado(
                org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication()
        ));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transaccionService.registrarRecarga(dto));
    }

    @PreAuthorize("hasAnyRole('SOPORTE', 'PASAJERO')")
    @GetMapping("/{id}")
    public ResponseEntity<TransaccionResponseDTO> obtenerPagoPorId(@PathVariable Long id) {
        TransaccionResponseDTO tx = transaccionService.obtenerTransaccionPorId(id);

        if (!transaccionSecurityService.esDuenioOEsSoporte(tx.getIdUsuario()))
            throw new UsuarioException(ErrorCodeEnum.ACCESO_DENEGADO);

        return ResponseEntity.ok(tx);
    }


    @PreAuthorize("@transaccionSecurityService.esDuenioOEsSoporte(#idUsuario)")
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<TransaccionResponseDTO>> obtenerPagosPorUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(transaccionService.obtenerTransaccionesPorUsuario(idUsuario));
    }

    @PreAuthorize("hasRole('SOPORTE')")
    @GetMapping("/fechas")
    public ResponseEntity<List<TransaccionResponseDTO>> obtenerPagosGlobalesPorFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        return ResponseEntity.ok(transaccionService.obtenerTransaccionesPorFechas(fechaInicio, fechaFin));
    }

    //Filtro para buscar transacciones por varios criterios como monto y fechas
    @PreAuthorize("@transaccionSecurityService.esDuenioOEsSoporte(#idUsuario)")
    @GetMapping("/usuario/{idUsuario}/buscar")
    public ResponseEntity<List<TransaccionResponseDTO>> buscarTransacciones(
            @PathVariable Long idUsuario,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
            @RequestParam(required = false) BigDecimal min,
            @RequestParam(required = false) BigDecimal max,
            Authentication auth) {

        return ResponseEntity.ok(transaccionService.obtenerTransaccionesAvanzadas(idUsuario, fechaInicio, fechaFin, min, max));
    }

    @PreAuthorize("hasRole('SOPORTE')")
    @GetMapping("/recarga/usuario/medio-pago/{medioPago}")
    public ResponseEntity<List<Recarga>> obtenerPagosPorMedioPago(@PathVariable MedioPagoEnum medioPago) {
        return ResponseEntity.ok(transaccionService.obtenerRecargasPorMedioPago(medioPago));
    }

    @PreAuthorize("hasRole('SOPORTE')")
    @GetMapping("/usuario/documento/{numDocumento}")
    public ResponseEntity<List<TransaccionResponseDTO>> obtenerPagosPorNumDocumento(@PathVariable String numDocumento) {
        return ResponseEntity.ok(transaccionService.obtenerTransaccionPorNumDocumentoUsuario(numDocumento));
    }

    @PreAuthorize("hasRole('SOPORTE')")
    @GetMapping("/usuario/nombre/{nombre}")
    public ResponseEntity<List<TransaccionResponseDTO>> obtenerPagosPorNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(transaccionService.obtenerTransaccionPorNombre(nombre));
    }


    @GetMapping("/usuario/{idUsuario}/fechas")
    public ResponseEntity<List<TransaccionResponseDTO>> obtenerPagosPorUsuarioYFechas(
            @PathVariable Long idUsuario,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        return ResponseEntity.ok(transaccionService.obtenerTransaccionesPorUsuarioYFechas(idUsuario, fechaInicio, fechaFin));
    }

    @PreAuthorize("hasRole('PASAJERO')")
    @PostMapping("/pasar-saldo")
    public ResponseEntity<?> pasarSaldo(
            @Valid @RequestBody PasarSaldoRequestDTO dto

    ) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long idUsuario = transaccionSecurityService.obtenerIdUsuarioAutenticado(authentication);
        String resultado = transaccionService.PasarSaldo(dto.getNumTelefono(), dto.getValor(), idUsuario);
        return ResponseEntity.ok(resultado);

    }
}
