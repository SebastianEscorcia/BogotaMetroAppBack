package com.sena.BogotaMetroApp.presentation.controller.soporte;


import com.sena.BogotaMetroApp.presentation.dto.soporte.SoporteRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.soporte.SoporteResponseDTO;
import com.sena.BogotaMetroApp.services.soporte.ISoporteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/soporte")
@RequiredArgsConstructor
public class SoporteController {
    private final ISoporteService soporteService;

    @PostMapping
    public ResponseEntity<@NotNull SoporteResponseDTO> registrar(@Valid @RequestBody SoporteRequestDTO dto) {
        return ResponseEntity.ok(soporteService.registrar(dto));
    }
}
