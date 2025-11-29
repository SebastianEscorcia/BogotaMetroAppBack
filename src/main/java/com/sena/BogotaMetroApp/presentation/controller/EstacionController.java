package com.sena.BogotaMetroApp.presentation.controller;


import com.sena.BogotaMetroApp.presentation.dto.estacion.EstacionRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.estacion.EstacionResponseDTO;
import com.sena.BogotaMetroApp.services.estacion.IEstacionServices;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estaciones")
@RequiredArgsConstructor
public class EstacionController {

    private final IEstacionServices estacionService;

    @PostMapping
    public EstacionResponseDTO crear(@RequestBody EstacionRequestDTO dto) {
        return estacionService.crear(dto);
    }

    @GetMapping("/{id}")
    public EstacionResponseDTO obtener(@PathVariable Long id) {
        return estacionService.obtener(id);
    }

    @GetMapping
    public List<EstacionResponseDTO> listar() {
        return estacionService.listar();
    }

    @PutMapping("/{id}")
    public EstacionResponseDTO actualizar(@PathVariable Long id, @RequestBody EstacionRequestDTO dto) {
        return estacionService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        estacionService.eliminar(id);
    }
}
