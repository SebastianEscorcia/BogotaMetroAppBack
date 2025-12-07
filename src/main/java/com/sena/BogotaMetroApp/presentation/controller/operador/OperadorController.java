package com.sena.BogotaMetroApp.presentation.controller.operador;

import com.sena.BogotaMetroApp.presentation.dto.operador.OperadorResponseDTO;
import com.sena.BogotaMetroApp.presentation.dto.operador.RegistroOperadorDTO;
import com.sena.BogotaMetroApp.services.operador.IOperadorService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/operador")
@RequiredArgsConstructor
public class OperadorController {
    private final IOperadorService operadorService;

    @PostMapping("/registro")
    public ResponseEntity<@NotNull OperadorResponseDTO> registrar(@Valid @RequestBody RegistroOperadorDTO dto) {
        return ResponseEntity.ok(operadorService.registrar(dto));
    }
}
