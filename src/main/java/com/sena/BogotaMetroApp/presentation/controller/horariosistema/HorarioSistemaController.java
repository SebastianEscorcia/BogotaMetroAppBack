package com.sena.BogotaMetroApp.presentation.controller.horariosistema;

import com.sena.BogotaMetroApp.presentation.dto.horariosistema.HorarioSistemaRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.horariosistema.HorarioSistemaResponseDTO;
import com.sena.BogotaMetroApp.services.horariosistema.IHorarioSistemaService;
import com.sena.BogotaMetroApp.utils.enums.DiaSemana;
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

import java.util.List;

@RestController
@RequestMapping("/api/horarios-sistema")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Horarios Sistema", description = "Gestión de horarios de operación del Metro")
public class HorarioSistemaController {

    private final IHorarioSistemaService horarioService;

    @Operation(summary = "Crear horario del sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Horario creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })

    @PostMapping
    public ResponseEntity<HorarioSistemaResponseDTO> crearHorario(@Valid @RequestBody HorarioSistemaRequestDTO request) {
        HorarioSistemaResponseDTO response = horarioService.crearHorario(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Obtener horario por día")
    @GetMapping("/dia/{dia}")
    public ResponseEntity<HorarioSistemaResponseDTO> obtenerHorarioPorDia(@PathVariable DiaSemana dia) {
        HorarioSistemaResponseDTO response = horarioService.obtenerHorarioPorDia(dia);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener todos los horarios")
    @GetMapping
    public ResponseEntity<List<HorarioSistemaResponseDTO>> obtenerTodosHorarios() {
        List<HorarioSistemaResponseDTO> response = horarioService.obtenerTodosHorarios();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Actualizar horario")
    @PutMapping("/{id}")
    public ResponseEntity<HorarioSistemaResponseDTO> actualizarHorario(@PathVariable Long id, @Valid @RequestBody HorarioSistemaRequestDTO request) {
        HorarioSistemaResponseDTO response = horarioService.actualizarHorario(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar horario")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarHorario(@PathVariable Long id) {
        horarioService.eliminarHorario(id);
        return ResponseEntity.noContent().build();
    }
}