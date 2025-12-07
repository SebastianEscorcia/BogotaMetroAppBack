package com.sena.BogotaMetroApp.presentation.controller.interrupcion;

import com.sena.BogotaMetroApp.presentation.dto.interrupcion.InterrupcionRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.interrupcion.InterrupcionResponseDTO;
import com.sena.BogotaMetroApp.presentation.dto.interrupcion.InterrupcionUpdateDTO;
import com.sena.BogotaMetroApp.services.interrupcion.IInterrupcionServices;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/interrupciones")
@RequiredArgsConstructor
public class InterrupcionController {
    private final IInterrupcionServices service;

    @PostMapping
    public InterrupcionResponseDTO crear(@RequestBody InterrupcionRequestDTO dto) {
        return service.crear(dto);
    }

    @GetMapping
    public List<InterrupcionResponseDTO> listar() {
        return service.listar();
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

    @PatchMapping("/{id}/solucionar")
    public void solucionar(@PathVariable Long id) {
        service.marcarComoSolucionada(id);
    }

    @PutMapping("/{id}")
    public InterrupcionResponseDTO actualizar(@PathVariable Long id, @RequestBody InterrupcionUpdateDTO dto) {
        return service.actualizar(id, dto);
    }

}
