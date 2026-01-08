package com.sena.BogotaMetroApp.persistence.repository.supportfaq;

import com.sena.BogotaMetroApp.persistence.models.supportfaq.CategoryFaq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryFaqRepository extends JpaRepository<CategoryFaq,Long> {
    Optional<CategoryFaq> findByIdAndActiveTrue(Long id);
    List<CategoryFaq> findAllByActiveTrue();
}
