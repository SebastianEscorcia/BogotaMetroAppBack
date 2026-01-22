package com.sena.BogotaMetroApp.externalservices.cache;

import com.sena.BogotaMetroApp.presentation.dto.supportfaq.CategoryFaqResponseDTO;

import java.util.List;
import java.util.Optional;

public interface ICategoryFaqCacheService {
    void cacheCategoryFaqs(List<CategoryFaqResponseDTO> categories);

    Optional<List<CategoryFaqResponseDTO>> getCachedCategoryFaqs();

    void invalidateCategoryFaqsCache();
}
