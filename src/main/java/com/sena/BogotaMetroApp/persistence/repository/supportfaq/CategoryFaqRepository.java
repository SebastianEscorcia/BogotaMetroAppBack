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

    // Para validar duplicados al crear: busca si existe otra categoría activa con el mismo nombre
    boolean existsByNameIgnoreCaseAndActiveTrue(String name);

    // Para validar duplicados al actualizar: busca si existe otra categoría activa con el mismo nombre, excluyendo el ID actual
    boolean existsByNameIgnoreCaseAndActiveTrueAndIdNot(String name, Long id);
}
