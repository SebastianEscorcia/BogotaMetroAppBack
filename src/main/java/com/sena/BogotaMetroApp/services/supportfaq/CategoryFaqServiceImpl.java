package com.sena.BogotaMetroApp.services.supportfaq;

import com.sena.BogotaMetroApp.externalservices.cache.ICategoryFaqCacheService;
import com.sena.BogotaMetroApp.mapper.supportfaq.CategoryFaqMapper;
import com.sena.BogotaMetroApp.persistence.models.supportfaq.CategoryFaq;
import com.sena.BogotaMetroApp.persistence.repository.supportfaq.CategoryFaqRepository;
import com.sena.BogotaMetroApp.presentation.dto.supportfaq.CategoryFaqRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.supportfaq.CategoryFaqResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryFaqServiceImpl implements ICategoryFaqService {
    private final CategoryFaqRepository catFaqRepository;
    private final CategoryFaqMapper catFaqMapper;
    private final ICategoryFaqCacheService categoryCacheService;

    @Override
    @Transactional
    public CategoryFaqResponseDTO createCategoryFaq(CategoryFaqRequestDTO dto) {
        CategoryFaq categoryFaq = catFaqMapper.toEntity(dto);
        categoryFaq.setActive(true);
        CategoryFaq saved = catFaqRepository.save(categoryFaq);
        categoryCacheService.invalidateCategoryFaqsCache();
        return catFaqMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryFaqResponseDTO getCategoryFaqById(Long id) {
        CategoryFaq categoryFaq = catFaqRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("CategoryFaq no encontrada con id: " + id));
        return catFaqMapper.toDto(categoryFaq);
    }

    @Override
    @Transactional
    public CategoryFaqResponseDTO updateCategoryFaq(Long id, CategoryFaqRequestDTO dto) {
        CategoryFaq categoryFaq = catFaqRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("CategoryFaq no encontrada con id: " + id));

        categoryFaq.setName(dto.getName());
        CategoryFaq updated = catFaqRepository.save(categoryFaq);
        categoryCacheService.invalidateCategoryFaqsCache();
        return catFaqMapper.toDto(updated);
    }

    @Override
    @Transactional
    public List<CategoryFaqResponseDTO> findAllByCategoryId(Long categoryId) {
        return catFaqRepository.findAllByActiveTrue().stream().map(catFaqMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteCategoryFaq(Long id) {
        CategoryFaq categoryFaq = catFaqRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("CategoryFaq no encontrada con id: " + id));

        categoryFaq.setActive(false);
        catFaqRepository.save(categoryFaq);
        categoryCacheService.invalidateCategoryFaqsCache();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryFaqResponseDTO> getAllActiveCategoryFaqs() {
        return categoryCacheService.getCachedCategoryFaqs()
                .orElseGet(() -> {
                    // 2. Cache miss - consultar DB
                    log.info("Cache miss - consultando Category FAQs desde DB");
                    List<CategoryFaqResponseDTO> categories = catFaqRepository.findAllByActiveTrue()
                            .stream()
                            .map(catFaqMapper::toDto)
                            .collect(Collectors.toList());

                    // 3. Guardar en cache
                    categoryCacheService.cacheCategoryFaqs(categories);

                    return categories;
                });
    }
}

