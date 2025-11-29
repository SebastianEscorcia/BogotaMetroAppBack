package com.sena.BogotaMetroApp.presentation.controller.qr;

import com.sena.BogotaMetroApp.presentation.dto.qr.QrRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.qr.QrResponseDTO;
import com.sena.BogotaMetroApp.presentation.dto.qr.ValidarQrRequest;
import com.sena.BogotaMetroApp.presentation.dto.qr.ValidarQrResponse;
import com.sena.BogotaMetroApp.services.qr.IQrService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @PostMapping("/generar")
    public ResponseEntity<QrResponseDTO> generarQr(
            @Valid @RequestBody QrRequestDTO request) {
        QrResponseDTO qr = qrService.generarQr(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(qr);
    }

    @Operation(summary = "Validar QR en torniquete",
            description = "Verifica si un QR es válido para ingresar al metro")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "QR válido - Acceso permitido"),
            @ApiResponse(responseCode = "403", description = "QR inválido - Acceso denegado"),
            @ApiResponse(responseCode = "404", description = "QR no encontrado")
    })
    @PostMapping("/validar")
    public ResponseEntity<ValidarQrResponse> validarQrEnTorniquete(
            @Valid @RequestBody ValidarQrRequest request) {
        ValidarQrResponse response = qrService.validarQrEnTorniquete(request);

        if (!response.isPermitido()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Regenerar QR expirado",
            description = "Genera un nuevo QR para un viaje cuyo QR anterior expiró")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "QR regenerado exitosamente"),
            @ApiResponse(responseCode = "400", description = "QR anterior aún válido o ya fue usado"),
            @ApiResponse(responseCode = "404", description = "Pago o QR anterior no encontrado")
    })
    @PostMapping("/regenerar/pago/{idPago}")
    public ResponseEntity<QrResponseDTO> regenerarQrViaje(
            @PathVariable Long idPago) {
        QrResponseDTO nuevoQr = qrService.regenerarQrViaje(idPago);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoQr);
    }
}
