package com.sena.BogotaMetroApp.presentation.controller.supportfaq;

import com.sena.BogotaMetroApp.presentation.dto.supportfaq.CategoryFaqRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.supportfaq.CategoryFaqResponseDTO;
import com.sena.BogotaMetroApp.services.supportfaq.ICategoryFaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category-faqs")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SOPORTE')")
public class CategoryFaqController {
    private final ICategoryFaqService categoryFaqService;


    @PostMapping
    public ResponseEntity<CategoryFaqResponseDTO> createCategoryFaq(@RequestBody CategoryFaqRequestDTO dto) {
        CategoryFaqResponseDTO response = categoryFaqService.createCategoryFaq(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryFaqResponseDTO> getCategoryFaqById(@PathVariable Long id) {
        CategoryFaqResponseDTO response = categoryFaqService.getCategoryFaqById(id);
        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasRole('PASAJERO')")
    @GetMapping
    public ResponseEntity<List<CategoryFaqResponseDTO>> getAllActiveCategoryFaqs() {
        List<CategoryFaqResponseDTO> response = categoryFaqService.getAllActiveCategoryFaqs();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryFaqResponseDTO> updateCategoryFaq(
            @PathVariable Long id,
            @RequestBody CategoryFaqRequestDTO dto) {
        CategoryFaqResponseDTO response = categoryFaqService.updateCategoryFaq(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoryFaq(@PathVariable Long id) {
        categoryFaqService.deleteCategoryFaq(id);
        return ResponseEntity.noContent().build();
    }
}
