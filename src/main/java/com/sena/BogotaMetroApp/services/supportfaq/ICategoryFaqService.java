package com.sena.BogotaMetroApp.services.supportfaq;

import com.sena.BogotaMetroApp.presentation.dto.supportfaq.CategoryFaqRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.supportfaq.CategoryFaqResponseDTO;

import java.util.List;

public interface ICategoryFaqService {
    CategoryFaqResponseDTO createCategoryFaq(CategoryFaqRequestDTO categoryFaqRequestDTO);

    CategoryFaqResponseDTO getCategoryFaqById(Long id);

    CategoryFaqResponseDTO updateCategoryFaq(Long id, CategoryFaqRequestDTO categoryFaqRequestDTO);

    List<CategoryFaqResponseDTO> findAllByCategoryId(Long categoryId);

    void deleteCategoryFaq(Long id);

    List<CategoryFaqResponseDTO> getAllActiveCategoryFaqs();
}
