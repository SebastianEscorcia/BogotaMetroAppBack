package com.sena.BogotaMetroApp.presentation.controller.pasajeroviaje;

import com.sena.BogotaMetroApp.presentation.dto.pasajeroviaje.PasajeroViajeRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.pasajeroviaje.PasajeroViajeResponseDTO;
import com.sena.BogotaMetroApp.services.pasajeroviaje.IPasajeroViajeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pasajero-viaje")
@RequiredArgsConstructor
public class PasajeroViajeController {
    private final IPasajeroViajeService pasajeroViajeService;

    @PostMapping("/registrar")
    public Object registrar(@RequestBody PasajeroViajeRequestDTO dto) {
        return pasajeroViajeService.registrarViaje(dto);
    }

    @GetMapping("/{idPasajero}/{idViaje}")
    public Object obtenerTicket(@PathVariable Long idPasajero, @PathVariable Long idViaje) {
        return pasajeroViajeService.obtenerTicket(idPasajero, idViaje);
    }

    @GetMapping("/por-pasajero/{idPasajero}")
    public ResponseEntity<List<PasajeroViajeResponseDTO>> viajesPorPasajero(@PathVariable Long idPasajero) {
        return ResponseEntity.ok(pasajeroViajeService.obtenerViajesPorPasajero(idPasajero));
    }

    @GetMapping("/por-viaje/{idViaje}")
    public ResponseEntity<List<PasajeroViajeResponseDTO>> pasajerosPorViaje(@PathVariable Long idViaje) {
        return ResponseEntity.ok(pasajeroViajeService.obtenerPasajerosDeViaje(idViaje));
    }

}
