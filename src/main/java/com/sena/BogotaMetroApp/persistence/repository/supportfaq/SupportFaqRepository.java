package com.sena.BogotaMetroApp.persistence.repository.supportfaq;

import com.sena.BogotaMetroApp.persistence.models.supportfaq.SupportFaq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupportFaqRepository extends JpaRepository<SupportFaq,Long> {
    Optional<SupportFaq> findByIdAndIsActiveTrue(Long id);
    List<SupportFaq> findAllByIsActiveTrue();
    List<SupportFaq> findAllByCategoryFaqIdAndIsActiveTrue(Long categoryId);
    Boolean existsByQuestionIgnoreCaseAndCategoryFaqIdAndIsActiveTrue(String question, Long categoryId);
    Boolean existsByQuestionIgnoreCaseAndCategoryFaqIdAndIsActiveTrueAndIdNot(String question, Long categoryId, Long id);
}

