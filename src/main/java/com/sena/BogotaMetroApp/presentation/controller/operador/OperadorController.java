package com.sena.BogotaMetroApp.presentation.controller.operador;

import com.sena.BogotaMetroApp.presentation.dto.operador.OperadorResponseDTO;
import com.sena.BogotaMetroApp.presentation.dto.operador.RegistroOperadorDTO;
import com.sena.BogotaMetroApp.services.operador.IOperadorService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/operador")
@RequiredArgsConstructor
public class OperadorController {
    private final IOperadorService operadorService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/registro")
    public ResponseEntity<@NotNull OperadorResponseDTO> registrar(@Valid @RequestBody RegistroOperadorDTO dto) {
        return ResponseEntity.ok(operadorService.registrar(dto));
    }

    @GetMapping
    public ResponseEntity<List<OperadorResponseDTO>> listar(@RequestParam(required = false) String busqueda) {
        return ResponseEntity.ok(operadorService.listarOperadores(busqueda));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OperadorResponseDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(operadorService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OperadorResponseDTO> actualizar(@PathVariable Long id, @RequestBody RegistroOperadorDTO dto) {
        return ResponseEntity.ok(operadorService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        operadorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reactivar")
    public ResponseEntity<Void> reactivar(@PathVariable Long id) {
        operadorService.reactivar(id);
        return ResponseEntity.ok().build();
    }
}
