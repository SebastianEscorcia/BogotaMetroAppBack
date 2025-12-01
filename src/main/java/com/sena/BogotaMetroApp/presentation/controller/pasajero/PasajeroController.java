package com.sena.BogotaMetroApp.presentation.controller.pasajero;

import com.sena.BogotaMetroApp.presentation.dto.pasajero.PasajeroRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.pasajero.PasajeroResponseDTO;
import com.sena.BogotaMetroApp.presentation.dto.pasajero.RegistroPasajeroUnificadoDTO;
import com.sena.BogotaMetroApp.services.pasajero.IPasajeroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "api/pasajero", produces = "application/json")
@RequiredArgsConstructor
public class PasajeroController {
    private final IPasajeroService pasajeroService;

    @PostMapping("/registro")
    public ResponseEntity<PasajeroResponseDTO> registroUnico(@Valid @RequestBody RegistroPasajeroUnificadoDTO dto, UriComponentsBuilder uriBuilder) {
        PasajeroResponseDTO created = pasajeroService.registrarConUsuario(dto);

        URI location = uriBuilder.path("/api/pasajero/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PasajeroResponseDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(pasajeroService.obtener(id));
    }

    @GetMapping
    public ResponseEntity<List<PasajeroResponseDTO>> listar() {
        return ResponseEntity.ok(pasajeroService.listarTodos());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pasajeroService.eliminar(id);
        return ResponseEntity.noContent().build();
    }


}
