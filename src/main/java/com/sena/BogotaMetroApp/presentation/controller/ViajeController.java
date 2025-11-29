package com.sena.BogotaMetroApp.presentation.controller;


import com.sena.BogotaMetroApp.presentation.dto.viaje.ViajeRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.viaje.ViajeResponseDTO;
import com.sena.BogotaMetroApp.services.viaje.IViajeServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/viajes")
@RequiredArgsConstructor
public class ViajeController {

    private final IViajeServices viajeService;

    @PostMapping
    public ResponseEntity<ViajeResponseDTO> crear(@RequestBody ViajeRequestDTO dto) {
        return ResponseEntity.ok(viajeService.crearViaje(dto));
    }

    @GetMapping
    public ResponseEntity<List<ViajeResponseDTO>> listar() {
        return ResponseEntity.ok(viajeService.listarViajes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ViajeResponseDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(viajeService.obtenerViaje(id));
    }
}


