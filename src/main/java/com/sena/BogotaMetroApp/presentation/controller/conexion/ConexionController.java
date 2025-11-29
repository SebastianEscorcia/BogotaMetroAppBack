package com.sena.BogotaMetroApp.presentation.controller.conexion;

import com.sena.BogotaMetroApp.presentation.dto.conexion.ConexionRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.conexion.ConexionResponseDTO;
import com.sena.BogotaMetroApp.services.conexion.IConexionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/conexiones")
@RequiredArgsConstructor

public class ConexionController {
    private final IConexionService servicio;

    @PostMapping
    public ResponseEntity<ConexionResponseDTO> crear(@Valid @RequestBody ConexionRequestDTO dto) {
        return ResponseEntity.ok(servicio.crear(dto));
    }

    @GetMapping
    public List<ConexionResponseDTO> listar() {
        return servicio.listar();
    }

}
