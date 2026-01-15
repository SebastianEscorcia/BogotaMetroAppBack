package com.sena.BogotaMetroApp.presentation.controller.supportfaq;

import com.sena.BogotaMetroApp.presentation.dto.supportfaq.SupportFaqRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.supportfaq.SupportFaqResponseDTO;
import com.sena.BogotaMetroApp.services.supportfaq.ISupportFaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/support-faqs")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SOPORTE')")
public class SupportFaqController {
    private final ISupportFaqService supportFaqService;

    @PostMapping
    public ResponseEntity<SupportFaqResponseDTO> createSupportFaq(@RequestBody SupportFaqRequestDTO dto) {
        SupportFaqResponseDTO response = supportFaqService.createSupportFaq(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupportFaqResponseDTO> getSupportFaqById(@PathVariable Long id) {
        SupportFaqResponseDTO response = supportFaqService.getSupportFaqById(id);
        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasRole('PASAJERO')")
    @GetMapping
    public ResponseEntity<List<SupportFaqResponseDTO>> getAllActiveSupportFaqs() {
        List<SupportFaqResponseDTO> response = supportFaqService.getAllActiveSupportFaqs();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<SupportFaqResponseDTO>> getSupportFaqsByCategory(@PathVariable Long categoryId) {
        List<SupportFaqResponseDTO> response = supportFaqService.getSupportFaqsByCategoryId(categoryId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupportFaqResponseDTO> updateSupportFaq(
            @PathVariable Long id,
            @RequestBody SupportFaqRequestDTO dto) {
        SupportFaqResponseDTO response = supportFaqService.updateSupportFaq(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupportFaq(@PathVariable Long id) {
        supportFaqService.deleteSupportFaq(id);
        return ResponseEntity.noContent().build();
    }
}
