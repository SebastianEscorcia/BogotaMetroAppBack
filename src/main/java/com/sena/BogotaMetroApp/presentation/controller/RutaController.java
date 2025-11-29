package com.sena.BogotaMetroApp.presentation.controller;

import com.sena.BogotaMetroApp.presentation.dto.ruta.RutaRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.ruta.RutaResponseDTO;
import com.sena.BogotaMetroApp.services.ruta.IRutaServices;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rutas")
@RequiredArgsConstructor
public class RutaController {

    private final IRutaServices rutaService;


    @PostMapping
    public RutaResponseDTO crear(@RequestBody RutaRequestDTO dto) {
        return rutaService.crear(dto);
    }

    @GetMapping("/{id}")
    public RutaResponseDTO obtener(@PathVariable Long id) {
        return rutaService.obtener(id);
    }

    @GetMapping
    public List<RutaResponseDTO> listar() {
        return rutaService.listar();
    }

    @PutMapping("/{id}")
    public RutaResponseDTO actualizar(@PathVariable Long id, @RequestBody RutaRequestDTO dto) {
        return rutaService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        rutaService.eliminar(id);
    }
}
