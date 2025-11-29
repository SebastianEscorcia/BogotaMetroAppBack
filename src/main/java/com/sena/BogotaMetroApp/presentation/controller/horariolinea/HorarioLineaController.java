package com.sena.BogotaMetroApp.presentation.controller.horariolinea;


import com.sena.BogotaMetroApp.presentation.dto.horariolinea.HorarioLineaRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.horariolinea.HorarioLineaResponseDTO;
import com.sena.BogotaMetroApp.services.horariolinea.IHorarioLineaServices;
import lombok.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/horarios-lineas")
@RequiredArgsConstructor
public class HorarioLineaController {
    private final IHorarioLineaServices service;

    @PostMapping
    public HorarioLineaResponseDTO crear(@RequestBody HorarioLineaRequestDTO dto) {
        return service.crear(dto);
    }

    @GetMapping
    public List<HorarioLineaResponseDTO> listar() {
        return service.listar();
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
