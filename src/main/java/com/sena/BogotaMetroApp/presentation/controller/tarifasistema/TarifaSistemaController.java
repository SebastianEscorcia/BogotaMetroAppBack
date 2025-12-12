package com.sena.BogotaMetroApp.presentation.controller.tarifasistema;

import com.sena.BogotaMetroApp.presentation.dto.tarifasistema.TarifaSistemaRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.tarifasistema.TarifaSistemaResponseDTO;
import com.sena.BogotaMetroApp.services.tarifasistema.ITarifaSistemaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tarifas-sistema")
@RequiredArgsConstructor
@Tag(name = "Tarifas Sistema", description = "Gestión de tarifas del Metro")
public class TarifaSistemaController {

    private final ITarifaSistemaService tarifaService;

    @Operation(summary = "Crear nueva tarifa (desactiva la anterior)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tarifa creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PreAuthorize("hasRole('ADMIN')")  // Solo admins cambian tarifas
    @PostMapping
    public ResponseEntity<TarifaSistemaResponseDTO> crearTarifa(@Valid @RequestBody TarifaSistemaRequestDTO request) {
        TarifaSistemaResponseDTO response = tarifaService.crearTarifa(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Obtener tarifa activa actual")
    @GetMapping("/activa")
    public ResponseEntity<TarifaSistemaResponseDTO> obtenerTarifaActiva() {
        TarifaSistemaResponseDTO response = tarifaService.obtenerTarifaActiva();
        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar tarifa existente")
    @PutMapping("/{id}")
    public ResponseEntity<TarifaSistemaResponseDTO> actualizarTarifa(@PathVariable Long id, @Valid @RequestBody TarifaSistemaRequestDTO request) {
        TarifaSistemaResponseDTO response = tarifaService.actualizarTarifa(id, request);
        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar tarifa")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTarifa(@PathVariable Long id) {
        tarifaService.eliminarTarifa(id);
        return ResponseEntity.noContent().build();
    }
}