package com.sena.BogotaMetroApp.mapper.supportfaq;


import com.sena.BogotaMetroApp.persistence.models.supportfaq.CategoryFaq;
import com.sena.BogotaMetroApp.presentation.dto.supportfaq.CategoryFaqRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.supportfaq.CategoryFaqResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class CategoryFaqMapper {
    public  CategoryFaqResponseDTO toDto(CategoryFaq supportFaq) {
        CategoryFaqResponseDTO dto = new CategoryFaqResponseDTO();
        dto.setId(supportFaq.getId());
        dto.setName(supportFaq.getName());
        dto.setActive(supportFaq.isActive());
        return dto;
    }
    public  CategoryFaq toEntity(CategoryFaqRequestDTO dto) {
        CategoryFaq categoryFaq = new CategoryFaq();
        categoryFaq.setName(dto.getName());
        categoryFaq.setActive(true);
        return categoryFaq;
    }
}
