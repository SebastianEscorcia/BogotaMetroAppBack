package com.sena.BogotaMetroApp.mapper.supportfaq;

import com.sena.BogotaMetroApp.persistence.models.supportfaq.SupportFaq;
import com.sena.BogotaMetroApp.presentation.dto.supportfaq.SupportFaqRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.supportfaq.SupportFaqResponseDTO;
import org.springframework.stereotype.Component;


@Component
public class SupportFaqMapper {

    public SupportFaqResponseDTO toDTO(SupportFaq supportFaq) {
        return new SupportFaqResponseDTO(
                supportFaq.getId(),
                supportFaq.getQuestion(),
                supportFaq.getAnswer(),
                supportFaq.isActive(),
                supportFaq.getCategoryFaq().getId()
        );
    }
    public SupportFaq toEntity(SupportFaqRequestDTO dto) {
        SupportFaq supportFaq = new SupportFaq();
        supportFaq.setQuestion(dto.getQuestion());
        supportFaq.setAnswer(dto.getAnswer());
        supportFaq.setActive(dto.isActive());
        return supportFaq;
    }
}
