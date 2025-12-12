package com.sena.BogotaMetroApp.presentation.controller.soporte;


import com.sena.BogotaMetroApp.presentation.dto.soporte.SoporteRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.soporte.SoporteResponseDTO;
import com.sena.BogotaMetroApp.presentation.dto.soporte.SoporteUpdateDTO;
import com.sena.BogotaMetroApp.services.soporte.ISoporteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/soporte")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class SoporteController {
    private final ISoporteService soporteService;

    @PostMapping
    public ResponseEntity<@NotNull SoporteResponseDTO> registrar(@Valid @RequestBody SoporteRequestDTO dto) {
        return ResponseEntity.ok(soporteService.registrar(dto));
    }
    @GetMapping
    public ResponseEntity<List<SoporteResponseDTO>> listar(@RequestParam(required = false) String busqueda) {
        return ResponseEntity.ok(soporteService.listarSoportes(busqueda));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SoporteResponseDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(soporteService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SoporteResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody SoporteUpdateDTO dto) {
        return ResponseEntity.ok(soporteService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        soporteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
