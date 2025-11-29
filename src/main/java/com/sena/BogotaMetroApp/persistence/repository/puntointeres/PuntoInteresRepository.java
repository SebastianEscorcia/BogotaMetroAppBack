package com.sena.BogotaMetroApp.persistence.repository.puntointeres;

import com.sena.BogotaMetroApp.persistence.models.puntointeres.PuntoInteres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PuntoInteresRepository extends JpaRepository<PuntoInteres, Long> {
}
