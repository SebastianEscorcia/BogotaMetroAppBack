package com.sena.BogotaMetroApp.presentation.controller.qr;

import com.sena.BogotaMetroApp.presentation.dto.qr.QrResponseDTO;
import com.sena.BogotaMetroApp.services.qr.IQrService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/qr")
@RequiredArgsConstructor
@Tag(name = "QR", description = "Gestión de códigos QR para acceso al metro")
public class QrController {

    private final IQrService qrService;

    @Operation(summary = "Generar código QR",
            description = "Crea un nuevo código QR para pago o viaje")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "QR creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuario/Pago/Viaje no encontrado")
    })
    @PreAuthorize("hasRole('PASAJERO')")
    @PostMapping("/generar")
    public ResponseEntity<QrResponseDTO> generarQr(
            Authentication authentication) {
        String email = authentication.getName();
        QrResponseDTO qr = qrService.generarQrAcceso(email);
        return ResponseEntity.status(HttpStatus.CREATED).body(qr);
    }


}
