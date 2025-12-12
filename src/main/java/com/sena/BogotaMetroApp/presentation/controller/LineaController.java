package com.sena.BogotaMetroApp.presentation.controller;

import com.sena.BogotaMetroApp.presentation.dto.linea.LineaRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.linea.LineaResponseDTO;
import com.sena.BogotaMetroApp.services.linea.ILineaServices;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lineas")
@PreAuthorize("hasRole('OPERADOR')")
@RequiredArgsConstructor
public class LineaController {
    private final ILineaServices lineaService;

    @PostMapping
    public LineaResponseDTO crear(@RequestBody LineaRequestDTO dto) {
        return lineaService.crear(dto);
    }

    @GetMapping("/{id}")
    public LineaResponseDTO obtener(@PathVariable Long id) {
        return lineaService.obtener(id);
    }

    @PreAuthorize("hasAnyRole('OPERADOR','PASAJERO')")
    @GetMapping
    public List<LineaResponseDTO> listar() {
        return lineaService.listar();
    }

    @PutMapping("/{id}")
    public LineaResponseDTO actualizar(
            @PathVariable Long id,
            @RequestBody LineaRequestDTO dto
    ) {
        return lineaService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        lineaService.eliminar(id);
    }
}
