package com.sena.BogotaMetroApp.presentation.controller.pasajero;


import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.presentation.dto.pasajero.PasajeroResponseDTO;
import com.sena.BogotaMetroApp.presentation.dto.pasajero.PasajeroUpdateDTO;
import com.sena.BogotaMetroApp.presentation.dto.pasajero.RegistroPasajeroUnificadoDTO;
import com.sena.BogotaMetroApp.services.pasajero.IPasajeroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/api/pasajero", produces = "application/json")
@RequiredArgsConstructor
public class PasajeroController {
    private final IPasajeroService pasajeroService;

    @PostMapping("/registro")
    public ResponseEntity<PasajeroResponseDTO> registroUnico(@Valid @RequestBody RegistroPasajeroUnificadoDTO dto, UriComponentsBuilder uriBuilder) {
        PasajeroResponseDTO created = pasajeroService.registrarConUsuario(dto);

        URI location = uriBuilder.path("/api/pasajero/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PreAuthorize("hasAnyRole('ADMIN','PASAJERO')")
    @GetMapping("/{id}")
    public ResponseEntity<PasajeroResponseDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(pasajeroService.obtener(id));
    }

    @PreAuthorize("hasAnyRole('SOPORTE', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<PasajeroResponseDTO>> listar() {
        return ResponseEntity.ok(pasajeroService.listarTodos());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PASAJERO')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pasajeroService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('PASAJERO')")
    public ResponseEntity<PasajeroResponseDTO> obtenerMisDatos() {
        String correoAutenticado = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(pasajeroService.obtenerPorCorreo(correoAutenticado));
    }
    @PreAuthorize("hasRole('PASAJERO')")
    @PutMapping
    public ResponseEntity<PasajeroResponseDTO> actualizar( @Valid @RequestBody PasajeroUpdateDTO dto) {

        String correo = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return ResponseEntity.ok(pasajeroService.actualizar(correo, dto));
    }

}
