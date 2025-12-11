package com.sena.BogotaMetroApp.presentation.controller.puntointeres;

import com.sena.BogotaMetroApp.presentation.dto.puntointeres.PuntoInteresRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.puntointeres.PuntoInteresResponseDTO;
import com.sena.BogotaMetroApp.services.puntointeres.IPuntoInteresService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/poi")
@RequiredArgsConstructor
public class PuntoInteresController {
    private final IPuntoInteresService servicio;

    @PreAuthorize("hasRole('OPERADOR')")
    @PostMapping
    public ResponseEntity<PuntoInteresResponseDTO> crear(@Valid @RequestBody PuntoInteresRequestDTO dto) {
        return ResponseEntity.ok(servicio.crear(dto));
    }

    @PreAuthorize("hasAnyRole('OPERADOR','PASAJERO')")
    @GetMapping
    public List<PuntoInteresResponseDTO> listar() {
        return servicio.listar();
    }
}
