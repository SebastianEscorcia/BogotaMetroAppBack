package com.sena.BogotaMetroApp.services.supportfaq;

import com.sena.BogotaMetroApp.presentation.dto.supportfaq.SupportFaqRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.supportfaq.SupportFaqResponseDTO;

import java.util.List;

public interface ISupportFaqService {
    SupportFaqResponseDTO createSupportFaq(SupportFaqRequestDTO supportFaqRequestDTO);
    SupportFaqResponseDTO getSupportFaqById(Long id);
    List<SupportFaqResponseDTO> getAllActiveSupportFaqs();
    List<SupportFaqResponseDTO> getSupportFaqsByCategoryId(Long categoryId);
    SupportFaqResponseDTO updateSupportFaq(Long id, SupportFaqRequestDTO supportFaqRequestDTO);
    void deleteSupportFaq(Long id); // Soft delete
}
