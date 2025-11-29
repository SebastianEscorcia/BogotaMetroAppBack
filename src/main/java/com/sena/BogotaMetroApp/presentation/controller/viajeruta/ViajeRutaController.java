package com.sena.BogotaMetroApp.presentation.controller.viajeruta;


import com.sena.BogotaMetroApp.presentation.dto.viajeruta.ViajeRutaRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.viajeruta.ViajeRutaResponseDTO;
import com.sena.BogotaMetroApp.services.viajeruta.IViajeRuta;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/viajes-rutas")
@RequiredArgsConstructor
public class ViajeRutaController {
    private final IViajeRuta servicio;

    @PostMapping
    public ResponseEntity<@NotNull ViajeRutaResponseDTO> asignar(@Valid @RequestBody ViajeRutaRequestDTO dto) {
        return ResponseEntity.ok(servicio.asignarRuta(dto));
    }
}
