package com.sena.BogotaMetroApp.services.supportfaq;

import com.sena.BogotaMetroApp.mapper.supportfaq.SupportFaqMapper;
import com.sena.BogotaMetroApp.persistence.models.supportfaq.CategoryFaq;
import com.sena.BogotaMetroApp.persistence.models.supportfaq.SupportFaq;
import com.sena.BogotaMetroApp.persistence.repository.supportfaq.CategoryFaqRepository;
import com.sena.BogotaMetroApp.persistence.repository.supportfaq.SupportFaqRepository;
import com.sena.BogotaMetroApp.presentation.dto.supportfaq.SupportFaqRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.supportfaq.SupportFaqResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class SupportFaqServiceImpl implements ISupportFaqService {
    private final SupportFaqMapper supportFaqMapper;
    private final SupportFaqRepository supportFaqRepository;
    private final CategoryFaqRepository categoryFaqRepository;

    @Override
    @Transactional
    public SupportFaqResponseDTO createSupportFaq(SupportFaqRequestDTO dto) {
        CategoryFaq categoryFaq = categoryFaqRepository.findByIdAndActiveTrue(dto.getCategoryFaqId())
                .orElseThrow(() -> new EntityNotFoundException("CategoryFaq no encontrada con id: " + dto.getCategoryFaqId()));
        SupportFaq supportFaq = supportFaqMapper.toEntity(dto);
        supportFaq.setCategoryFaq(categoryFaq);
        supportFaq.setActive(true);
        SupportFaq saved = supportFaqRepository.save(supportFaq);
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
        return supportFaqRepository.findAllByIsActiveTrue()
                .stream()
                .map(supportFaqMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupportFaqResponseDTO> getSupportFaqsByCategoryId(Long categoryId) {
        return supportFaqRepository.findAllByCategoryFaqIdAndIsActiveTrue(categoryId)
                .stream()
                .map(supportFaqMapper::toDTO)
                .collect(Collectors.toList());
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
        return supportFaqMapper.toDTO(updated);
    }



    @Override
    public void deleteSupportFaq(Long id) {
        SupportFaq supportFaq = supportFaqRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("SupportFaq no encontrada con id: " + id));

        supportFaq.setActive(false);
        supportFaqRepository.save(supportFaq);
    }
}
