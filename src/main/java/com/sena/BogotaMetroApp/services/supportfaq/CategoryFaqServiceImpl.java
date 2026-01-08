package com.sena.BogotaMetroApp.services.supportfaq;

import com.sena.BogotaMetroApp.mapper.supportfaq.CategoryFaqMapper;
import com.sena.BogotaMetroApp.persistence.models.supportfaq.CategoryFaq;
import com.sena.BogotaMetroApp.persistence.repository.supportfaq.CategoryFaqRepository;
import com.sena.BogotaMetroApp.presentation.dto.supportfaq.CategoryFaqRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.supportfaq.CategoryFaqResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryFaqServiceImpl implements ICategoryFaqService {
    private final CategoryFaqRepository catFaqRepository;
    private final CategoryFaqMapper catFaqMapper;

    @Override
    @Transactional
    public CategoryFaqResponseDTO createCategoryFaq(CategoryFaqRequestDTO dto) {
        CategoryFaq categoryFaq = catFaqMapper.toEntity(dto);
        categoryFaq.setActive(true);
        CategoryFaq saved = catFaqRepository.save(categoryFaq);
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
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryFaqResponseDTO> getAllActiveCategoryFaqs() {
        return catFaqRepository.findAllByActiveTrue()
                .stream()
                .map(catFaqMapper::toDto)
                .collect(Collectors.toList());
    }
}
