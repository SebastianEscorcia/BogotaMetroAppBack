package com.sena.BogotaMetroApp.presentation.controller.pasarela;

import com.sena.BogotaMetroApp.presentation.dto.pasarela.PasarelaRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.pasarela.PasarelaResponseDTO;
import com.sena.BogotaMetroApp.services.pasarela.IPasarelaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pasarelas")
@RequiredArgsConstructor
public class PasarelaController {

    private final IPasarelaService pasarelaService;

    @PostMapping
    public ResponseEntity<PasarelaResponseDTO> crearPasarela(@RequestBody PasarelaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pasarelaService.crearPasarela(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PasarelaResponseDTO> obtenerPasarelaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pasarelaService.obtenerPasarelaPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<PasarelaResponseDTO>> obtenerTodasLasPasarelas() {
        return ResponseEntity.ok(pasarelaService.obtenerTodasLasPasarelas());
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<PasarelaResponseDTO> obtenerPasarelaPorNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(pasarelaService.obtenerPasarelaPorNombre(nombre));
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<PasarelaResponseDTO> obtenerPasarelaPorCodigo(@PathVariable Integer codigo) {
        return ResponseEntity.ok(pasarelaService.obtenerPasarelaPorCodigo(codigo));
    }

    @GetMapping("/pais/{pais}")
    public ResponseEntity<List<PasarelaResponseDTO>> obtenerPasarelasPorPais(@PathVariable String pais) {
        return ResponseEntity.ok(pasarelaService.obtenerPasarelasPorPais(pais));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PasarelaResponseDTO> actualizarPasarela(
            @PathVariable Long id,
            @RequestBody PasarelaRequestDTO dto) {
        return ResponseEntity.ok(pasarelaService.actualizarPasarela(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPasarela(@PathVariable Long id) {
        pasarelaService.eliminarPasarela(id);
        return ResponseEntity.noContent().build();
    }
}
