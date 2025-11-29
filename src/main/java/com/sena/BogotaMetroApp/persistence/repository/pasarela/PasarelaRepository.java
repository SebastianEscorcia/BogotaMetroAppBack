package com.sena.BogotaMetroApp.persistence.repository.pasarela;

import com.sena.BogotaMetroApp.persistence.models.pasarela.Pasarela;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PasarelaRepository extends JpaRepository<Pasarela, Long> {

    Optional<Pasarela> findByNombre(String nombre);

    Optional<Pasarela> findByCodigo(Integer codigo);

    List<Pasarela> findByPais(String pais);

    boolean existsByNombre(String nombre);

    boolean existsByCodigo(Integer codigo);
}
