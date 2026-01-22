package com.sena.BogotaMetroApp.externalservices.cache;

import com.sena.BogotaMetroApp.presentation.dto.supportfaq.SupportFaqResponseDTO;

import java.util.List;
import java.util.Optional;

public interface ISupportFaqCacheService {
    void cacheSupportFaqs(List<SupportFaqResponseDTO> faqs);

    Optional<List<SupportFaqResponseDTO>> getCachedSupportFaqs();

    void cacheSupportFaqsByCategory(Long categoryId, List<SupportFaqResponseDTO> faqs);

    Optional<List<SupportFaqResponseDTO>> getCachedSupportFaqsByCategory(Long categoryId);

    void invalidateSupportFaqsCache();
}
