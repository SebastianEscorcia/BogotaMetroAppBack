package com.sena.BogotaMetroApp.persistence.repository.puntointeres;

import com.sena.BogotaMetroApp.persistence.models.puntointeres.PuntoInteres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PuntoInteresRepository extends JpaRepository<PuntoInteres, Long> {
    List<PuntoInteres> findByActivoTrue();
}
