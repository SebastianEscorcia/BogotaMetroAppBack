package com.sena.BogotaMetroApp.presentation.controller.torniquete;

import com.sena.BogotaMetroApp.presentation.dto.torniquete.ValidarIngresoDTO;
import com.sena.BogotaMetroApp.services.torniquete.ITorniqueteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/torniquete")
@RequiredArgsConstructor
public class TorniqueteController {

    private final ITorniqueteService torniqueteService;

    @PostMapping("/validar-ingreso")
    public ResponseEntity<String> validarIngreso(@RequestBody ValidarIngresoDTO request) {
        torniqueteService.procesarIngreso(request.getContenidoQr(), request.getIdEstacion());
        return ResponseEntity.ok("Ingreso autorizado. Torniquete abierto.");
    }
}
