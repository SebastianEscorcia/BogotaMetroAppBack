package com.sena.BogotaMetroApp.presentation.controller;

import com.sena.BogotaMetroApp.presentation.dto.estacionlinea.EstacionLineaRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.estacionlinea.EstacionLineaResponseDTO;
import com.sena.BogotaMetroApp.services.estacion.IEstacionLineaServices;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estaciones-lineas")
@RequiredArgsConstructor
public class EstacionLineaController {

    private final IEstacionLineaServices service;

    @PostMapping
    public EstacionLineaResponseDTO crear(@RequestBody EstacionLineaRequestDTO dto) {
        return service.crear(dto);
    }

    @GetMapping
    public List<EstacionLineaResponseDTO> listar() {
        return service.listar();
    }

    @DeleteMapping("/{idLinea}/{idEstacion}")
    public void eliminar(@PathVariable Long idLinea, @PathVariable Long idEstacion) {
        service.eliminar(idLinea, idEstacion);
    }
}
