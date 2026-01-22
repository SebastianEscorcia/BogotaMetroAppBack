package com.sena.BogotaMetroApp.services.supportfaq;

import com.sena.BogotaMetroApp.externalservices.cache.ISupportFaqCacheService;
import com.sena.BogotaMetroApp.mapper.supportfaq.SupportFaqMapper;
import com.sena.BogotaMetroApp.persistence.models.supportfaq.CategoryFaq;
import com.sena.BogotaMetroApp.persistence.models.supportfaq.SupportFaq;
import com.sena.BogotaMetroApp.persistence.repository.supportfaq.CategoryFaqRepository;
import com.sena.BogotaMetroApp.persistence.repository.supportfaq.SupportFaqRepository;
import com.sena.BogotaMetroApp.presentation.dto.supportfaq.SupportFaqRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.supportfaq.SupportFaqResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class SupportFaqServiceImpl implements ISupportFaqService {
    private final SupportFaqMapper supportFaqMapper;
    private final SupportFaqRepository supportFaqRepository;
    private final CategoryFaqRepository categoryFaqRepository;
    private  final ISupportFaqCacheService supportFaqCache;

    @Override
    @Transactional
    public SupportFaqResponseDTO createSupportFaq(SupportFaqRequestDTO dto) {
        CategoryFaq categoryFaq = categoryFaqRepository.findByIdAndActiveTrue(dto.getCategoryFaqId())
                .orElseThrow(() -> new EntityNotFoundException("CategoryFaq no encontrada con id: " + dto.getCategoryFaqId()));
        SupportFaq supportFaq = supportFaqMapper.toEntity(dto);
        supportFaq.setCategoryFaq(categoryFaq);
        supportFaq.setActive(true);
        SupportFaq saved = supportFaqRepository.save(supportFaq);
        supportFaqCache.invalidateSupportFaqsCache();
        return supportFaqMapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public SupportFaqResponseDTO getSupportFaqById(Long id) {

        SupportFaq supportFaq = supportFaqRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("SupportFaq no encontrada con id: " + id));
        return supportFaqMapper.toDTO(supportFaq);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupportFaqResponseDTO> getAllActiveSupportFaqs() {
        return supportFaqCache.getCachedSupportFaqs()
                .orElseGet(() -> {
                    // 2. Cache miss - consultar DB
                    log.info("Cache miss - consultando Support FAQs desde DB");
                    List<SupportFaqResponseDTO> faqs = supportFaqRepository.findAllByIsActiveTrue()
                            .stream()
                            .map(supportFaqMapper::toDTO)
                            .collect(Collectors.toList());

                    // 3. Guardar en cache
                    supportFaqCache.cacheSupportFaqs(faqs);

                    return faqs;
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupportFaqResponseDTO> getSupportFaqsByCategoryId(Long categoryId) {
        // 1. Intentar obtener desde cache
        return supportFaqCache.getCachedSupportFaqsByCategory(categoryId)
                .orElseGet(() -> {
                    // 2. Cache miss - consultar DB
                    log.info("Cache miss - consultando Support FAQs por categoría {} desde DB", categoryId);
                    List<SupportFaqResponseDTO> faqs = supportFaqRepository.findAllByCategoryFaqIdAndIsActiveTrue(categoryId)
                            .stream()
                            .map(supportFaqMapper::toDTO)
                            .collect(Collectors.toList());

                    // 3. Guardar en cache
                    supportFaqCache.cacheSupportFaqsByCategory(categoryId, faqs);

                    return faqs;
                });
    }

    @Override
    public SupportFaqResponseDTO updateSupportFaq(Long id, SupportFaqRequestDTO dto) {
        SupportFaq supportFaq = supportFaqRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("SupportFaq no encontrada con id: " + id));

        if (dto.getCategoryFaqId() != null) {
            CategoryFaq categoryFaq = categoryFaqRepository.findByIdAndActiveTrue(dto.getCategoryFaqId())
                    .orElseThrow(() -> new EntityNotFoundException("CategoryFaq no encontrada con id: " + dto.getCategoryFaqId()));
            supportFaq.setCategoryFaq(categoryFaq);
        }

        supportFaq.setQuestion(dto.getQuestion());
        supportFaq.setAnswer(dto.getAnswer());

        SupportFaq updated = supportFaqRepository.save(supportFaq);
        supportFaqCache.invalidateSupportFaqsCache();

        return supportFaqMapper.toDTO(updated);
    }



    @Override
    public void deleteSupportFaq(Long id) {
        SupportFaq supportFaq = supportFaqRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("SupportFaq no encontrada con id: " + id));

        supportFaq.setActive(false);
        supportFaqRepository.save(supportFaq);
        supportFaqCache.invalidateSupportFaqsCache();
    }
}
